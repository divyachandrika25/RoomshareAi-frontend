import { createContext, useContext, useState, useEffect } from 'react';
import { authAPI, profileAPI } from '../lib/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('auth_token'));
  const [loading, setLoading] = useState(true);
  const [isPremium, setIsPremium] = useState(localStorage.getItem('is_premium') === 'true');
  const [aiUsage, setAiUsage] = useState(parseInt(localStorage.getItem('ai_usage_count') || '0'));

  useEffect(() => {
    const email = localStorage.getItem('user_email');
    if (token && email) {
      profileAPI.getDashboard(email)
        .then((res) => {
          if (res.data?.success) {
            const profile = res.data.data?.profile;
            const premium = profile?.is_premium === true;
            setIsPremium(premium);
            localStorage.setItem('is_premium', premium);
            setUser({ email, ...profile, ...res.data.data });
          }
        })
        .catch(() => {})
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [token]);

  const login = async (email, password) => {
    const res = await authAPI.login({ email, password });
    if (res.data?.success && res.data?.token) {
      localStorage.setItem('auth_token', res.data.token);
      localStorage.setItem('user_email', email);
      const userObj = res.data.user || {};
      const premium = userObj.is_premium === true;
      localStorage.setItem('is_premium', premium);
      setIsPremium(premium);
      setToken(res.data.token);
      setUser({ email, ...userObj });
      return { success: true, isPremium: premium };
    }
    return { success: false, error: res.data?.error || res.data?.message || 'Login failed' };
  };

  const register = async (data) => {
    const res = await authAPI.register(data);
    return res.data;
  };

  const logout = () => {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_email');
    setToken(null);
    setUser(null);
  };

  const refreshUser = async () => {
    const email = localStorage.getItem('user_email');
    if (!email) return;
    try {
      const res = await profileAPI.getDashboard(email);
      if (res.data?.success) {
        const profile = res.data.data?.profile;
        const premium = profile?.is_premium === true;
        setIsPremium(premium);
        localStorage.setItem('is_premium', premium);
        setUser({ email, ...profile, ...res.data.data });
      }
    } catch {}
  };

  const setPremium = async (val) => {
    localStorage.setItem('is_premium', val);
    setIsPremium(val);
    if (user?.email) {
      try {
        await profileAPI.updateSubscription({ email: user.email, is_premium: val });
      } catch (e) {
        console.error('Failed to sync premium status to backend', e);
      }
    }
  };

  const incrementAiUsage = () => {
    const newVal = aiUsage + 1;
    localStorage.setItem('ai_usage_count', newVal);
    setAiUsage(newVal);
    return newVal;
  };

  return (
    <AuthContext.Provider value={{ 
      user, token, loading, login, register, logout, refreshUser, 
      isPremium, setPremium, aiUsage, incrementAiUsage 
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
