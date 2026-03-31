import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Lock, ArrowRight, Loader2, ShieldCheck, CheckCircle2 } from 'lucide-react';
import { motion } from 'framer-motion';

const ResetPasswordScreen = () => {
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const navigate = useNavigate();

  const handleReset = (e) => {
    e.preventDefault();
    if (!password || password !== confirmPassword) return;

    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      setIsSuccess(true);
    }, 2000);
  };

  return (
    <div className="p-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary-500/10 blur-[100px] rounded-full" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-purple-500/10 blur-[100px] rounded-full" />

      <div className="glass-panel p-8 md:p-10 rounded-3xl w-full max-w-md relative z-10 border border-slate-200 shadow-2xl">
        <div className="mb-8">
          <h1 className="text-3xl font-bold font-heading mb-2 text-black">
            {isSuccess ? 'Password Changed!' : 'Reset Password'}
          </h1>
          <p className="text-slate-600 font-medium">
            {isSuccess 
              ? "Your password has been successfully reset. You can now log in with your new password." 
              : "Choose a strong password to protect your account."
            }
          </p>
        </div>

        {!isSuccess ? (
          <form onSubmit={handleReset} className="space-y-5">
            <div>
              <label className="text-sm font-bold text-slate-700 block mb-2 px-1">New Password</label>
              <div className="relative">
                <Lock size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                <input 
                  type="password" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  placeholder="••••••••"
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 pl-12 pr-4 text-black outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                />
              </div>
            </div>

            <div>
              <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Confirm New Password</label>
              <div className="relative">
                <Lock size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                <input 
                  type="password" 
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  required
                  placeholder="••••••••"
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 pl-12 pr-4 text-black outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                />
              </div>
              {password && confirmPassword && password !== confirmPassword && (
                <p className="text-red-500 text-xs mt-2 px-1 font-bold">Passwords do not match</p>
              )}
            </div>

            <button 
              type="submit" 
              disabled={isLoading || !password || password !== confirmPassword}
              className="w-full bg-gradient-to-r from-primary-600 to-primary-500 hover:from-primary-500 hover:to-primary-400 text-white p-4 rounded-xl font-bold transition-all shadow-lg shadow-primary-500/25 flex items-center justify-center gap-2 group disabled:opacity-70 disabled:cursor-not-allowed mt-4"
            >
              {isLoading ? (
                <Loader2 size={20} className="animate-spin" />
              ) : (
                <>
                  Reset Password
                  <ArrowRight size={18} className="group-hover:translate-x-1 transition-transform" />
                </>
              )}
            </button>
          </form>
        ) : (
          <motion.div 
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="text-center py-4"
          >
            <div className="w-20 h-20 bg-green-50 border border-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-8 shadow-inner">
              <CheckCircle2 size={32} />
            </div>
            
            <button 
              onClick={() => navigate('/login')}
              className="w-full bg-primary-600 hover:bg-primary-700 text-white p-4 rounded-xl font-bold transition-all shadow-lg hover:scale-[1.02]"
            >
              Back to Login
            </button>
          </motion.div>
        )}
      </div>
    </div>
  );
};

export default ResetPasswordScreen;
