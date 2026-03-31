import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Shield, ArrowRight, Loader2, MessageSquare } from 'lucide-react';
import { motion } from 'framer-motion';

const OTPVerificationScreen = () => {
  const [otp, setOtp] = useState(['', '', '', '', '', '']);
  const [isLoading, setIsLoading] = useState(false);
  const [timer, setTimer] = useState(59);
  const inputs = useRef([]);
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(() => {
      setTimer(prev => prev > 0 ? prev - 1 : 0);
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  const handleChange = (element, index) => {
    if (isNaN(element.value)) return false;
    setOtp([...otp.map((d, idx) => (idx === index ? element.value : d))]);
    if (element.nextSibling) {
      element.nextSibling.focus();
    }
  };

  const handleVerify = () => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate('/home');
    }, 1500);
  };

  return (
    <div className="p-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary-500/10 blur-[100px] rounded-full" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-purple-500/10 blur-[100px] rounded-full" />

      <div className="glass-panel p-8 md:p-10 rounded-3xl w-full max-w-md relative z-10 border border-slate-200 shadow-2xl">
        <div className="text-center mb-10">
          <div className="w-20 h-20 bg-primary-50 text-primary-600 border border-primary-100 rounded-full flex items-center justify-center mx-auto mb-6 shadow-inner">
            <Shield size={32} />
          </div>
          <h1 className="text-3xl font-bold font-heading mb-2 text-black">Security Code</h1>
          <p className="text-slate-600 font-medium">We've sent a 6-digit code to your phone. Enter it below to verify.</p>
        </div>

        <div className="flex justify-between gap-2 mb-8">
          {otp.map((data, index) => (
            <input
              key={index}
              type="text"
              maxLength="1"
              value={data}
              onChange={e => handleChange(e.target, index)}
              onFocus={e => e.target.select()}
              className="w-12 h-14 bg-slate-50 border-2 border-slate-200 rounded-xl text-center text-xl font-bold text-black outline-none focus:border-primary-500 focus:bg-white transition-all shadow-sm"
            />
          ))}
        </div>

        <button 
          onClick={handleVerify}
          disabled={isLoading || otp.join('').length < 6}
          className="w-full bg-primary-600 hover:bg-primary-700 text-white p-4 rounded-xl font-bold transition-all shadow-lg shadow-primary-500/25 flex items-center justify-center gap-2 disabled:opacity-70"
        >
          {isLoading ? <Loader2 size={20} className="animate-spin" /> : 'Verify Code'}
        </button>

        <div className="text-center mt-8">
          <p className="text-sm text-slate-500 font-medium flex items-center justify-center gap-2">
            Resend code in <span className="text-primary-600 font-bold">0:{timer < 10 ? `0${timer}` : timer}</span>
          </p>
          {timer === 0 && (
            <button className="text-primary-600 font-bold hover:underline mt-2 text-sm">Resend Now</button>
          )}
        </div>
      </div>
    </div>
  );
};

export default OTPVerificationScreen;
