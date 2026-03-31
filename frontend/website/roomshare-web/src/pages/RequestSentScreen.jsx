import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, CheckCircle2, Home, 
  MessageCircle, Send, Sparkles, 
  Clock, ShieldCheck, Mail
} from 'lucide-react';

const RequestSentScreen = () => {
  const navigate = useNavigate();

  const data = {
    title: "Request Sent!",
    subtitle: "Your booking request has been securely sent to the property owner. We'll notify you and your group once they respond.",
    ownerEmail: "landlord@example.com"
  };

  return (
    <div className="min-h-screen bg-white">
      {/* Progress atmosphere */}
      <div className="fixed top-0 left-0 w-full h-1 bg-slate-100 z-50">
        <motion.div 
          initial={{ width: '0%', x: '-100%' }}
          animate={{ x: '200%' }}
          transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
          className="w-1/3 h-full bg-primary-600"
        />
      </div>

      {/* Header */}
      <div className="px-6 py-6 border-b border-slate-100 flex items-center sticky top-0 bg-white z-40">
        <button 
          onClick={() => navigate(-1)}
          className="w-10 h-10 bg-slate-50 rounded-full flex items-center justify-center hover:bg-slate-100 transition-colors"
        >
          <ArrowLeft size={20} className="text-slate-600" />
        </button>
        <h1 className="flex-1 text-center font-bold text-black -ml-10">Available Rooms</h1>
      </div>

      <div className="max-w-xl mx-auto p-12 flex flex-col items-center justify-center pt-24">
        <motion.div 
          initial={{ scale: 0.5, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ type: "spring", stiffness: 200, damping: 12 }}
          className="relative mb-12"
        >
          <div className="w-32 h-32 bg-green-50 rounded-[40px] flex items-center justify-center shadow-inner">
            <Send size={56} className="text-green-600 -rotate-12" />
          </div>
          <motion.div 
            animate={{ scale: [1, 1.2, 1], opacity: [1, 0.5, 1] }}
            transition={{ duration: 2, repeat: Infinity }}
            className="absolute -top-2 -right-2 w-10 h-10 bg-white rounded-full flex items-center justify-center shadow-lg border border-slate-50"
          >
            <CheckCircle2 size={24} className="text-primary-600" />
          </motion.div>
        </motion.div>

        <div className="text-center space-y-4 mb-16">
          <h2 className="text-4xl font-black text-black font-heading tracking-tight">
            {data.title}
          </h2>
          <p className="text-slate-500 font-medium text-lg leading-relaxed max-w-sm mx-auto">
            {data.subtitle}
          </p>
        </div>

        {/* Status Tracker */}
        <div className="w-full bg-slate-50 border border-slate-100 rounded-[32px] p-8 space-y-8 mb-16">
           <div className="flex items-center gap-4">
              <div className="w-10 h-10 bg-primary-100 rounded-xl flex items-center justify-center text-primary-600 shrink-0">
                <Clock size={20} />
              </div>
              <div className="flex-1">
                <p className="text-sm font-bold text-black">Average response time: 2 hours</p>
                <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">LANDLORD PERFORMANCE</p>
              </div>
           </div>

           <div className="flex items-center gap-4">
              <div className="w-10 h-10 bg-green-100 rounded-xl flex items-center justify-center text-green-600 shrink-0">
                <ShieldCheck size={20} />
              </div>
              <div className="flex-1">
                <p className="text-sm font-bold text-black">Group Trust Score: Platinum</p>
                <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">VERIFIED STATUS</p>
              </div>
           </div>
        </div>

        <div className="w-full space-y-4">
          <button 
            onClick={() => navigate('/home')}
            className="w-full bg-black text-white p-5 rounded-2xl font-bold transition-all shadow-xl shadow-black/10 flex items-center justify-center gap-3 hover:scale-[1.02]"
          >
            <Home size={20} />
            Back to Home
          </button>
          
          <button 
            className="w-full bg-white border border-slate-200 text-slate-600 p-5 rounded-2xl font-bold transition-all hover:bg-slate-50 flex items-center justify-center gap-2 group"
          >
            <MessageCircle size={20} className="group-hover:text-primary-600 transition-colors" />
            Message Owner
          </button>
        </div>
        
        <div className="mt-12 flex items-center gap-2 text-[10px] font-black text-slate-400 uppercase tracking-widest">
          <Sparkles size={12} className="text-primary-400" />
          Powered by Roomshare AI Trust Engine
        </div>
      </div>
    </div>
  );
};

export default RequestSentScreen;
