import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { Mail, Lock, Eye, EyeOff, Home, ArrowRight, Sparkles } from 'lucide-react';
import toast from 'react-hot-toast';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPwd, setShowPwd] = useState(false);
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const validateEmail = (email) => {
    return String(email)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      );
  };

  const validatePassword = (password) => {
    return password.match(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{6,}$/);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email || !password) return toast.error('Please fill in all fields');
    if (!validateEmail(email)) return toast.error('Please enter a valid email address');
    if (!validatePassword(password)) return toast.error('Incorrect password format');
    
    setLoading(true);
    try {
      const res = await login(email, password);
      if (res.success) {
        toast.success('Welcome back!');
        if (res.isPremium) {
          navigate('/dashboard/home');
        } else {
          navigate('/dashboard/pricing');
        }
      } else {
        toast.error(res.error || 'Invalid credentials');
      }
    } catch (err) {
      toast.error(err?.response?.data?.error || 'Login failed');
    } finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen flex relative">
      {/* Logos at top corners */}
      <div className="absolute top-6 left-6 z-20 pointer-events-none">
        <img src="/assets/saveetha_logo.png" alt="Saveetha Logo" className="w-16 h-16 md:w-24 md:h-24 object-contain" />
      </div>
      <div className="absolute top-6 right-6 z-20 pointer-events-none">
        <img src="/assets/ses_logo.png" alt="SES Logo" className="w-16 h-16 md:w-24 md:h-24 object-contain" />
      </div>

      {/* Left - Form */}
      <div className="flex-1 flex items-center justify-center px-6 py-12">
        <div className="w-full max-w-md">
          <Link to="/" className="flex items-center gap-3 mb-10">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-600 to-violet-600 flex items-center justify-center">
              <Home className="w-5 h-5 text-white" />
            </div>
            <span className="text-xl font-extrabold text-foreground">RoomShare AI</span>
          </Link>
          <h1 className="text-3xl font-extrabold text-foreground mb-2">Welcome Back</h1>
          <p className="text-muted-foreground mb-8">Sign in to continue your roommate search</p>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="text-sm font-semibold text-foreground mb-1.5 block">Email</label>
              <div className="relative">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#94a3b8]" />
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Enter your email" className="w-full pl-12 pr-4 py-3.5 rounded-xl bg-background border border-border focus:border-[#1e63ff] focus:ring-2 focus:ring-indigo-500/20 outline-none text-sm font-medium transition-all" />
              </div>
            </div>
            <div>
              <label className="text-sm font-semibold text-foreground mb-1.5 block">Password</label>
              <div className="relative">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#94a3b8]" />
                <input type={showPwd ? 'text' : 'password'} value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Enter your password" className="w-full pl-12 pr-12 py-3.5 rounded-xl bg-background border border-border focus:border-[#1e63ff] focus:ring-2 focus:ring-indigo-500/20 outline-none text-sm font-medium transition-all" />
                <button type="button" onClick={() => setShowPwd(!showPwd)} className="absolute right-4 top-1/2 -translate-y-1/2 text-[#94a3b8] hover:text-muted-foreground">
                  {showPwd ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                </button>
              </div>
            </div>
            <div className="flex justify-end">
              <Link to="/forgot-password" className="text-sm font-semibold text-indigo-400 hover:underline">Forgot Password?</Link>
            </div>
            <button type="submit" disabled={loading} className="w-full py-3.5 bg-gradient-to-r from-indigo-600 to-violet-600 text-white font-bold rounded-xl hover:shadow-lg hover:shadow-indigo-500/20 transition-all flex items-center justify-center gap-2 disabled:opacity-50">
              {loading ? <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" /> : <><span>Sign In</span><ArrowRight className="w-5 h-5" /></>}
            </button>
          </form>

          <p className="mt-8 text-center text-sm text-muted-foreground">
            Don't have an account? <Link to="/register" className="font-bold text-indigo-400 hover:underline">Create one</Link>
          </p>
        </div>
      </div>

      {/* Right - Decorative */}
      <div className="hidden lg:flex flex-1 bg-gradient-to-br from-indigo-600 to-[#9d1efa] items-center justify-center p-12 relative overflow-hidden">
        <div className="absolute top-10 right-10 w-80 h-80 bg-white/10 rounded-full blur-3xl" />
        <div className="absolute bottom-10 left-10 w-60 h-60 bg-white/10 rounded-full blur-3xl" />
        <div className="text-center text-white relative z-10 max-w-md">
          <Sparkles className="w-16 h-16 mx-auto mb-6 opacity-80" />
          <h2 className="text-3xl font-extrabold mb-4">Smart Roommate Matching</h2>
          <p className="text-white/70 text-lg leading-relaxed">Our AI analyzes your lifestyle, budget, and preferences to find the perfect roommate — so you don't have to.</p>
          <div className="mt-10 grid grid-cols-3 gap-6">
            <div className="bg-white/10 rounded-xl p-4 backdrop-blur-sm">
              <p className="text-2xl font-bold">95%</p>
              <p className="text-xs text-white/60">Match Rate</p>
            </div>
            <div className="bg-white/10 rounded-xl p-4 backdrop-blur-sm">
              <p className="text-2xl font-bold">10K+</p>
              <p className="text-xs text-white/60">Users</p>
            </div>
            <div className="bg-white/10 rounded-xl p-4 backdrop-blur-sm">
              <p className="text-2xl font-bold">50+</p>
              <p className="text-xs text-white/60">Cities</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
