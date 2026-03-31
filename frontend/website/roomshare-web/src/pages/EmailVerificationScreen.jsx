import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Mail, ArrowRight, Loader2, RefreshCw, CheckCircle2 } from 'lucide-react';
import { motion } from 'framer-motion';

const EmailVerificationScreen = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [isResending, setIsResending] = useState(false);
  const [isVerified, setIsVerified] = useState(false);
  const navigate = useNavigate();

  const handleResend = () => {
    setIsResending(true);
    setTimeout(() => setIsResending(false), 2000);
  };

  const handleCheckStatus = () => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      setIsVerified(true);
    }, 1500);
  };

  return (
    <div className="p-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary-500/10 blur-[100px] rounded-full" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-purple-500/10 blur-[100px] rounded-full" />

      <div className="glass-panel p-8 md:p-10 rounded-3xl w-full max-w-md relative z-10 border border-slate-200 shadow-2xl">
        <div className="text-center mb-10">
          <div className={`w-20 h-20 rounded-full flex items-center justify-center mx-auto mb-6 shadow-lg ${
            isVerified ? 'bg-green-50 text-green-600 border border-green-100' : 'bg-primary-50 text-primary-600 border border-primary-100'
          }`}>
            {isVerified ? <CheckCircle2 size={32} /> : <Mail size={32} />}
          </div>
          <h1 className="text-3xl font-bold font-heading mb-2 text-black">
            {isVerified ? 'Email Verified!' : 'Verify Your Email'}
          </h1>
          <p className="text-slate-600 font-medium">
            {isVerified 
              ? "Awesome! Your identity has been confirmed. Let's finish your profile." 
              : "We've sent a verification link to your email. Click it to activate your account."
            }
          </p>
        </div>

        {!isVerified ? (
          <div className="space-y-4">
            <button 
              onClick={handleCheckStatus}
              disabled={isLoading}
              className="w-full bg-primary-600 hover:bg-primary-700 text-white p-4 rounded-xl font-bold transition-all shadow-lg flex items-center justify-center gap-2"
            >
              {isLoading ? <Loader2 size={20} className="animate-spin" /> : 'I Have Verified My Email'}
            </button>
            
            <button 
              onClick={handleResend}
              disabled={isResending}
              className="w-full bg-white border border-slate-200 text-black p-4 rounded-xl font-bold transition-all hover:bg-slate-50 flex items-center justify-center gap-2"
            >
              <RefreshCw size={18} className={isResending ? 'animate-spin' : ''} />
              {isResending ? 'Resending...' : 'Resend Email'}
            </button>
          </div>
        ) : (
          <button 
            onClick={() => navigate('/home')}
            className="w-full bg-black text-white p-4 rounded-xl font-bold transition-all shadow-lg hover:bg-slate-800 flex items-center justify-center gap-2"
          >
            Continue to App <ArrowRight size={18} />
          </button>
        )}
      </div>
    </div>
  );
};

export default EmailVerificationScreen;
