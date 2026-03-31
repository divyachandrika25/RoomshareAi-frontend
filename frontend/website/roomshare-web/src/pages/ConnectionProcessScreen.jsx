import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { CheckCircle2, MessageCircle, ArrowLeft, Send, Sparkles, UserPlus } from 'lucide-react';
import { useNavigate, useParams } from 'react-router-dom';

const ConnectionProcessScreen = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState('request'); // request, sending, success
  const [message, setMessage] = useState("Hi! I love your room listing and think we might be great roommates. I'm a clean and quiet person who loves coffee!");

  const handleSend = () => {
    setStep('sending');
    setTimeout(() => {
      setStep('success');
    }, 2000);
  };

  return (
    <div className="p-6 md:p-12 min-h-screen flex items-center justify-center relative overflow-hidden">
      {/* Background Ornaments */}
      <div className="absolute top-0 right-0 w-96 h-96 bg-primary-100 blur-[100px] rounded-full opacity-30" />
      <div className="absolute bottom-0 left-0 w-96 h-96 bg-purple-100 blur-[100px] rounded-full opacity-30" />

      <motion.div 
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        className="glass-panel max-w-lg w-full p-8 md:p-10 rounded-3xl border border-slate-200 shadow-2xl relative z-10"
      >
        <AnimatePresence mode="wait">
          {step === 'request' && (
            <motion.div key="request" initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }}>
              <div className="flex items-center gap-4 mb-8">
                <button onClick={() => navigate(-1)} className="p-2 hover:bg-slate-100 rounded-xl transition-colors border border-slate-200 shadow-sm">
                  <ArrowLeft size={20} className="text-black" />
                </button>
                <h1 className="text-2xl font-bold font-heading text-black">Connect with Jordan</h1>
              </div>

              <div className="flex items-center gap-4 mb-6 p-4 bg-primary-50 rounded-2xl border border-primary-200 font-bold text-slate-800 shadow-sm">
                <Sparkles size={20} className="text-primary-600 shrink-0" />
                <p className="text-sm">You have a <span className="text-primary-700 font-black">98% match</span>. Sending a personalized note increases your chances of a reply!</p>
              </div>

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2">Your Intro Message</label>
                <textarea 
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-2xl p-4 text-black font-bold placeholder-slate-400 focus:border-primary-500/50 focus:bg-white outline-none min-h-[150px] transition-all shadow-inner resize-none"
                />
              </div>

              <button 
                onClick={handleSend}
                className="w-full bg-black hover:bg-slate-800 text-white font-bold py-4 rounded-xl flex items-center justify-center gap-2 transition-all hover:scale-[1.02] shadow-xl"
              >
                <Send size={18} /> Send Connection Request
              </button>
            </motion.div>
          )}

          {step === 'sending' && (
            <motion.div key="sending" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} className="py-20 flex flex-col items-center text-center">
              <div className="w-16 h-16 border-4 border-slate-100 border-t-primary-600 rounded-full animate-spin mb-6" />
              <h2 className="text-2xl font-bold text-black mb-2">Sending Request...</h2>
              <p className="text-slate-600 font-bold">Our AI is optimizing your profile for the best match.</p>
            </motion.div>
          )}

          {step === 'success' && (
            <motion.div key="success" initial={{ opacity: 0, scale: 0.8 }} animate={{ opacity: 1, scale: 1 }} className="py-10 flex flex-col items-center text-center">
              <div className="w-20 h-20 bg-green-50 border border-green-200 rounded-full flex items-center justify-center mb-6 shadow-xl relative">
                <CheckCircle2 size={40} className="text-green-600" />
              </div>
              <h2 className="text-3xl font-bold text-black mb-2">Request Sent!</h2>
              <p className="text-slate-600 mb-8 max-w-xs mx-auto font-bold">Jordan will see your request and common lifestyle scores shortly.</p>
              
              <div className="flex flex-col gap-3 w-full">
                <button 
                  onClick={() => navigate('/messages')}
                  className="w-full bg-primary-600 text-white font-bold py-4 rounded-xl flex items-center justify-center gap-2 hover:bg-primary-700 transition-all shadow-xl"
                >
                  <MessageCircle size={18} /> Go to Inbox
                </button>
                <button 
                  onClick={() => navigate('/home')}
                  className="w-full bg-slate-100 hover:bg-slate-200 text-black font-bold py-4 rounded-xl transition-all border border-slate-300"
                >
                  Back to Home
                </button>
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </motion.div>
    </div>
  );
};

export default ConnectionProcessScreen;
