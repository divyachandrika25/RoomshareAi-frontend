import { useState } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { authAPI } from '../../lib/api';
import { Home, Mail, ArrowRight } from 'lucide-react';
import toast from 'react-hot-toast';

export default function VerifyOtpPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email || '';
  const isReset = location.state?.isReset || false;
  const [otp, setOtp] = useState('');
  const [loading, setLoading] = useState(false);

  const handleVerify = async (e) => {
    e.preventDefault();
    if (!otp || otp.length < 4) return toast.error('Enter a valid OTP');
    setLoading(true);
    try {
      const res = await authAPI.verifyOtp(email, otp);
      if (res.data?.success) {
        toast.success('Email verified!');
        if (isReset) {
          navigate('/reset-password', { state: { email, otp } });
        } else {
          navigate('/login');
        }
      } else {
        toast.error(res.data?.error || 'Invalid OTP');
      }
    } catch (err) {
      toast.error('Verification failed');
    } finally { setLoading(false); }
  };

  const handleResend = async () => {
    try {
      await authAPI.sendOtp(email);
      toast.success('OTP resent!');
    } catch { toast.error('Failed to resend'); }
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
        <div className="text-center mb-8">
          <div className="w-16 h-16 rounded-xl bg-indigo-500/10 flex items-center justify-center mx-auto mb-4">
            <Mail className="w-8 h-8 text-indigo-400" />
          </div>
          <h1 className="text-2xl font-extrabold text-foreground mb-2">Verify Your Email</h1>
          <p className="text-muted-foreground text-sm">We sent a code to <span className="font-bold text-indigo-400">{email}</span></p>
        </div>
        <form onSubmit={handleVerify} className="space-y-5">
          <input type="text" value={otp} onChange={(e) => setOtp(e.target.value)} placeholder="Enter OTP" maxLength={6} className="w-full px-4 py-4 rounded-xl bg-background border border-border focus:border-[#1e63ff] outline-none text-center text-2xl font-bold tracking-[0.5em] transition-all" />
          <button type="submit" disabled={loading} className="w-full py-3.5 bg-gradient-to-r from-indigo-600 to-violet-600 text-white font-bold rounded-xl hover:shadow-lg hover:shadow-indigo-500/20 transition-all flex items-center justify-center gap-2 disabled:opacity-50">
            {loading ? <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" /> : <><span>Verify</span><ArrowRight className="w-5 h-5" /></>}
          </button>
        </form>
        <button onClick={handleResend} className="w-full mt-4 text-center text-sm text-indigo-400 font-semibold hover:underline">Resend OTP</button>
      </div>
    </div>
  );
}
