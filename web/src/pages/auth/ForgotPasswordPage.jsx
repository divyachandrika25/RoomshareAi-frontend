import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authAPI } from '../../lib/api';
import { Home, Mail, ArrowRight } from 'lucide-react';
import toast from 'react-hot-toast';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email) return toast.error('Enter your email');
    setLoading(true);
    try {
      const res = await authAPI.forgotPassword(email);
      if (res.data?.success) {
        toast.success('OTP sent to your email!');
        navigate('/verify-otp', { state: { email, isReset: true } });
      } else {
        toast.error(res.data?.error || 'Failed to send OTP');
      }
    } catch (err) {
      toast.error('Something went wrong');
    } finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background px-4">
      <div className="w-full max-w-md bg-card rounded-xl p-8 shadow-xl shadow-blue-50 border border-border">
        <Link to="/" className="flex items-center gap-3 mb-8 justify-center">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-600 to-violet-600 flex items-center justify-center">
            <Home className="w-5 h-5 text-white" />
          </div>
          <span className="text-xl font-extrabold text-foreground">RoomShare AI</span>
        </Link>
        <h1 className="text-2xl font-extrabold text-foreground mb-2 text-center">Forgot Password</h1>
        <p className="text-muted-foreground text-sm mb-8 text-center">Enter your email and we'll send you a reset code</p>
        <form onSubmit={handleSubmit} className="space-y-5">
          <div className="relative">
            <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#94a3b8]" />
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Enter your email" className="w-full pl-12 pr-4 py-3.5 rounded-xl bg-background border border-border focus:border-[#1e63ff] outline-none text-sm font-medium transition-all" />
          </div>
          <button type="submit" disabled={loading} className="w-full py-3.5 bg-gradient-to-r from-indigo-600 to-violet-600 text-white font-bold rounded-xl hover:shadow-lg hover:shadow-indigo-500/20 transition-all flex items-center justify-center gap-2 disabled:opacity-50">
            {loading ? <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" /> : <><span>Send Reset Code</span><ArrowRight className="w-5 h-5" /></>}
          </button>
        </form>
        <Link to="/login" className="block mt-6 text-center text-sm text-indigo-400 font-semibold hover:underline">Back to Sign In</Link>
      </div>
    </div>
  );
}
