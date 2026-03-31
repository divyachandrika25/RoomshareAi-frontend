import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Phone, ArrowRight, Loader2, Globe } from 'lucide-react';
import { motion } from 'framer-motion';

const PhoneVerificationScreen = () => {
  const [phoneNumber, setPhoneNumber] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSendCode = (e) => {
    e.preventDefault();
    if (!phoneNumber) return;
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate('/otp-verify');
    }, 1500);
  };

  return (
    <div className="p-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary-500/10 blur-[100px] rounded-full" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-purple-500/10 blur-[100px] rounded-full" />

      <div className="glass-panel p-8 md:p-10 rounded-3xl w-full max-w-md relative z-10 border border-slate-200 shadow-2xl">
        <div className="text-center mb-10">
          <div className="w-20 h-20 bg-primary-50 text-primary-600 border border-primary-100 rounded-full flex items-center justify-center mx-auto mb-6 shadow-inner">
            <Phone size={32} />
          </div>
          <h1 className="text-3xl font-bold font-heading mb-2 text-black">Phone Verification</h1>
          <p className="text-slate-600 font-medium">Verify your phone number to enhance account security and trust.</p>
        </div>

        <form onSubmit={handleSendCode} className="space-y-6">
          <div>
            <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Mobile Number</label>
            <div className="flex gap-3">
              <div className="w-24 bg-slate-50 border border-slate-200 rounded-xl flex items-center justify-center gap-2 font-bold text-sm shadow-sm">
                <Globe size={14} className="text-slate-400" /> +1
              </div>
              <div className="relative flex-1">
                <input 
                  type="tel" 
                  value={phoneNumber}
                  onChange={(e) => setPhoneNumber(e.target.value)}
                  required
                  placeholder="(555) 000-0000"
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 px-4 text-black placeholder-slate-400 outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                />
              </div>
            </div>
          </div>

          <button 
            type="submit" 
            disabled={isLoading || !phoneNumber}
            className="w-full bg-gradient-to-r from-primary-600 to-primary-500 hover:from-primary-500 hover:to-primary-400 text-white p-4 rounded-xl font-bold transition-all shadow-lg shadow-primary-500/25 flex items-center justify-center gap-2 group disabled:opacity-70 mt-2"
          >
            {isLoading ? <Loader2 size={20} className="animate-spin" /> : (
              <>
                Send Verification Code
                <ArrowRight size={18} className="group-hover:translate-x-1 transition-transform" />
              </>
            )}
          </button>
        </form>

        <p className="text-center text-xs text-slate-500 mt-10 leading-relaxed">
          By continuing, you agree to receive an SMS for verification. Message and data rates may apply.
        </p>
      </div>
    </div>
  );
};

export default PhoneVerificationScreen;
