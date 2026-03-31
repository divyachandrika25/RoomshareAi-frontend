import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, ArrowLeft, ArrowRight, Loader2, ShieldCheck } from 'lucide-react';
import { motion } from 'framer-motion';

const ForgotPasswordScreen = () => {
  const [email, setEmail] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSent, setIsSent] = useState(false);
  const navigate = useNavigate();

  const handleReset = (e) => {
    e.preventDefault();
    if (!email) return;

    setIsLoading(true);
    // Simulate API call
    setTimeout(() => {
      setIsLoading(false);
      setIsSent(true);
    }, 1500);
  };

  return (
    <div className="p-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary-500/10 blur-[100px] rounded-full" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-purple-500/10 blur-[100px] rounded-full" />

      <div className="glass-panel p-8 md:p-10 rounded-3xl w-full max-w-md relative z-10 border border-slate-200 shadow-2xl">
        <div className="mb-8">
          <button 
            onClick={() => navigate('/login')}
            className="flex items-center gap-2 text-slate-500 hover:text-black transition-colors font-bold text-sm mb-6"
          >
            <ArrowLeft size={16} /> Back to Login
          </button>
          
          <h1 className="text-3xl font-bold font-heading mb-2 text-black">
            {isSent ? 'Check Your Email' : 'Forgot Password?'}
          </h1>
          <p className="text-slate-600 font-medium">
            {isSent 
              ? "We've sent a password reset link to your email address." 
              : "Don't worry! It happens. Please enter the email associated with your account."
            }
          </p>
        </div>

        {!isSent ? (
          <form onSubmit={handleReset} className="space-y-6">
            <div className="bg-primary-50 border border-primary-200 p-4 rounded-2xl flex gap-3 items-start">
              <ShieldCheck className="text-primary-600 shrink-0" size={20} />
              <p className="text-xs text-primary-800 font-medium leading-relaxed">
                We'll send a secure link to your email. Click the link to set a new password for your RoomShare account.
              </p>
            </div>

            <div>
              <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Email Address</label>
              <div className="relative">
                <Mail size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                <input 
                  type="email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  placeholder="you@example.com"
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 pl-12 pr-4 text-black placeholder-slate-400 outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                />
              </div>
            </div>

            <button 
              type="submit" 
              disabled={isLoading || !email}
              className="w-full bg-gradient-to-r from-primary-600 to-primary-500 hover:from-primary-500 hover:to-primary-400 text-white p-4 rounded-xl font-bold transition-all shadow-lg shadow-primary-500/25 flex items-center justify-center gap-2 group disabled:opacity-70 disabled:cursor-not-allowed"
            >
              {isLoading ? (
                <Loader2 size={20} className="animate-spin" />
              ) : (
                <>
                  Send Reset Link
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
            <div className="w-20 h-20 bg-green-50 border border-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-6 shadow-inner">
              <Mail size={32} />
            </div>
            
            <button 
              onClick={() => navigate('/login')}
              className="w-full bg-slate-900 hover:bg-black text-white p-4 rounded-xl font-bold transition-all shadow-lg"
            >
              Return to Login
            </button>
            
            <p className="mt-8 text-sm text-slate-500 font-medium">
              Didn't receive the email? Check your spam folder or{' '}
              <button onClick={handleReset} className="text-primary-600 font-bold hover:underline">
                try again
              </button>
            </p>
          </motion.div>
        )}
      </div>
    </div>
  );
};

export default ForgotPasswordScreen;
