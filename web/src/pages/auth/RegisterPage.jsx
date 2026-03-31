import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { Mail, Lock, Eye, EyeOff, Home, User, Briefcase, MapPin, ArrowRight, Sparkles } from 'lucide-react';
import toast from 'react-hot-toast';

const Field = ({ icon: Icon, label, name, form, set, type = 'text', placeholder, showPwd, setShowPwd }) => (
  <div>
    <label className="text-sm font-semibold text-foreground mb-1.5 block">{label}</label>
    <div className="relative">
      <Icon className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#94a3b8]" />
      <input 
        type={type === 'password' ? (showPwd ? 'text' : 'password') : type} 
        value={form[name]} 
        onChange={(e) => set(name, e.target.value)} 
        placeholder={placeholder} 
        className="w-full pl-12 pr-4 py-3 rounded-xl bg-background border border-border focus:border-[#1e63ff] focus:ring-2 focus:ring-indigo-500/20 outline-none text-sm font-medium transition-all" 
      />
      {type === 'password' && (
        <button type="button" onClick={() => setShowPwd(!showPwd)} className="absolute right-4 top-1/2 -translate-y-1/2 text-[#94a3b8]">
          {showPwd ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
        </button>
      )}
    </div>
  </div>
);

export default function RegisterPage() {
  const [form, setForm] = useState({ username: '', full_name: '', gender: 'Male', age: '', occupation: '', email: '', address: '', password: '', confirm_password: '' });
  const [showPwd, setShowPwd] = useState(false);
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();
  const set = (k, v) => setForm(prev => ({ ...prev, [k]: v }));

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
    const { full_name, username, email, password, confirm_password, age, occupation, address } = form;

    if (!full_name || !username || !email || !password || !confirm_password) {
      return toast.error('Please fill in all required fields');
    }
    if (!validateEmail(email)) return toast.error('Please enter a valid email address');
    if (!validatePassword(password)) return toast.error('Password must include Uppercase, Lowercase, Number & Special character (min 6 chars)');
    if (password !== confirm_password) return toast.error('Passwords do not match');
    
    const ageNum = parseInt(age);
    if (isNaN(ageNum) || ageNum < 18 || ageNum > 100) {
      return toast.error('Age must be between 18 and 100');
    }

    setLoading(true);
    try {
      const data = { ...form, age: ageNum };
      const res = await register(data);
      if (res?.success) {
        toast.success('Account created! Verify your email.');
        navigate('/verify-otp', { state: { email: form.email } });
      } else {
        toast.error(res?.error || res?.message || 'Registration failed');
      }
    } catch (err) {
      toast.error(err?.response?.data?.error || err?.response?.data?.message || 'Registration failed');
    } finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen flex">
      <div className="hidden lg:flex flex-1 bg-gradient-to-br from-[#9d1efa] to-[#1e63ff] items-center justify-center p-12 relative overflow-hidden">
        <div className="absolute top-10 left-10 w-80 h-80 bg-white/10 rounded-full blur-3xl" />
        <div className="text-center text-white relative z-10 max-w-md">
          <Sparkles className="w-16 h-16 mx-auto mb-6 opacity-80" />
          <h2 className="text-3xl font-extrabold mb-4">Join the Community</h2>
          <p className="text-white/70 text-lg leading-relaxed">Create your profile and let our AI find compatible roommates that match your lifestyle perfectly.</p>
        </div>
      </div>

      <div className="flex-1 flex items-center justify-center px-6 py-8 overflow-y-auto">
        <div className="w-full max-w-md">
          <Link to="/" className="flex items-center gap-3 mb-8">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-600 to-violet-600 flex items-center justify-center">
              <Home className="w-5 h-5 text-white" />
            </div>
            <span className="text-xl font-extrabold text-foreground">RoomShare AI</span>
          </Link>
          <h1 className="text-3xl font-extrabold text-foreground mb-2">Create Account</h1>
          <p className="text-muted-foreground mb-6">Start finding your perfect roommate today</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <Field icon={User} label="Full Name*" name="full_name" form={form} set={set} placeholder="Your name" />
              <Field icon={User} label="Username*" name="username" form={form} set={set} placeholder="Username" />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="text-sm font-semibold text-foreground mb-1.5 block">Gender</label>
                <select value={form.gender} onChange={(e) => set('gender', e.target.value)} className="w-full px-4 py-3 rounded-xl bg-background border border-border focus:border-[#1e63ff] outline-none text-sm font-medium">
                  <option>Male</option><option>Female</option><option>Other</option>
                </select>
              </div>
              <Field icon={User} label="Age" name="age" type="number" form={form} set={set} placeholder="25" />
            </div>
            <Field icon={Briefcase} label="Occupation" name="occupation" form={form} set={set} placeholder="Student / Engineer" />
            <Field icon={Mail} label="Email*" name="email" type="email" form={form} set={set} placeholder="you@example.com" />
            <Field icon={MapPin} label="Address" name="address" form={form} set={set} placeholder="City, Area" />
            <Field icon={Lock} label="Password*" name="password" type="password" form={form} set={set} showPwd={showPwd} setShowPwd={setShowPwd} placeholder="••••••••" />
            <Field icon={Lock} label="Confirm Password*" name="confirm_password" type="password" form={form} set={set} showPwd={showPwd} setShowPwd={setShowPwd} placeholder="••••••••" />

            <button type="submit" disabled={loading} className="w-full py-3.5 bg-gradient-to-r from-indigo-600 to-violet-600 text-white font-bold rounded-xl hover:shadow-lg hover:shadow-indigo-500/20 transition-all flex items-center justify-center gap-2 disabled:opacity-50 mt-2">
              {loading ? <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" /> : <><span>Create Account</span><ArrowRight className="w-5 h-5" /></>}
            </button>
          </form>
          <p className="mt-6 text-center text-sm text-muted-foreground">
            Already have an account? <Link to="/login" className="font-bold text-indigo-400 hover:underline">Sign In</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
