import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  CheckCircle2, Home, CreditCard, 
  ArrowRight, Sparkles, Star, ShieldCheck, 
  Info, MessageCircle 
} from 'lucide-react';

const BookingSuccessScreen = () => {
  const navigate = useNavigate();

  const data = {
    confirmationTitle: "Room Booked Successfully!",
    confirmationMessage: "Congratulations! You have officially secured your shared home. Our AI has verified your deposit.",
    totalDueNow: "$2,400.00",
    paymentMethodLast4: "4242",
    nextSteps: [
      "Check your email for the digital lease.",
      "Schedule your move-in date in the chat.",
      "Connect with your new roommates!"
    ]
  };

  return (
    <div className="min-h-screen bg-white">
      {/* Confetti-like Sparkles Background */}
      <div className="fixed inset-0 pointer-events-none overflow-hidden opacity-30">
        {[...Array(12)].map((_, i) => (
          <motion.div
            key={i}
            initial={{ y: -20, opacity: 0 }}
            animate={{ 
              y: [0, 800], 
              opacity: [0, 1, 0],
              x: Math.random() * 1000,
              rotate: Math.random() * 360
            }}
            transition={{ 
              duration: 3 + Math.random() * 2, 
              repeat: Infinity, 
              delay: Math.random() * 5 
            }}
            className="absolute top-0 text-primary-500"
          >
            <Sparkles size={16} />
          </motion.div>
        ))}
      </div>

      <div className="relative z-10 max-w-2xl mx-auto p-8 pt-16 flex flex-col items-center">
        <motion.div 
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ type: "spring", stiffness: 200, damping: 15 }}
          className="w-28 h-28 bg-green-500 rounded-full flex items-center justify-center mb-10 shadow-3xl shadow-green-500/30"
        >
          <CheckCircle2 size={56} className="text-white" />
        </motion.div>

        <div className="text-center space-y-4 mb-12">
          <h1 className="text-4xl font-bold font-heading text-black leading-tight">
            {data.confirmationTitle}
          </h1>
          <p className="text-slate-600 font-medium text-lg leading-relaxed max-w-md mx-auto">
            {data.confirmationMessage}
          </p>
        </div>

        {/* Payment Summary */}
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="w-full bg-primary-50/50 border border-primary-100 p-8 rounded-[40px] shadow-sm mb-6"
        >
          <div className="flex justify-between items-center mb-6">
             <div>
              <p className="text-[10px] font-black text-primary-600/60 uppercase tracking-widest mb-1">TOTAL SECURED</p>
              <p className="text-4xl font-black text-primary-700">{data.totalDueNow}</p>
            </div>
            <div className="w-16 h-16 bg-white rounded-2xl flex items-center justify-center text-primary-600 shadow-md">
              <CreditCard size={32} />
            </div>
          </div>
          
          <div className="flex items-center gap-3 bg-white p-4 rounded-2xl border border-primary-100/50">
            <ShieldCheck size={20} className="text-green-500" />
            <p className="text-sm font-bold text-slate-700">
              Paid via Card ending in <span className="text-black">**** {data.paymentMethodLast4}</span>
            </p>
          </div>
        </motion.div>

        {/* Next Steps Card */}
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
          className="w-full bg-slate-50 border border-slate-100 p-8 rounded-[40px] space-y-6"
        >
          <div className="flex items-center gap-3">
             <div className="w-10 h-10 bg-white rounded-xl flex items-center justify-center text-black shadow-sm">
              <Info size={20} />
            </div>
            <h3 className="text-lg font-bold text-black font-heading">Next Steps for Move-in</h3>
          </div>
          
          <div className="space-y-4">
             {data.nextSteps.map((step, i) => (
              <div key={i} className="flex gap-4 group">
                <div className="w-6 h-6 rounded-full bg-white border border-slate-200 flex items-center justify-center text-[10px] font-black text-slate-400 group-hover:bg-primary-600 group-hover:text-white transition-colors">
                  {i + 1}
                </div>
                <p className="text-slate-600 font-bold text-sm leading-relaxed">{step}</p>
              </div>
             ))}
          </div>
        </motion.div>

        {/* Actions */}
        <div className="w-full pt-12 space-y-4">
          <button 
            onClick={() => navigate('/home')}
            className="w-full bg-primary-600 hover:bg-primary-700 text-white p-5 rounded-3xl font-bold transition-all shadow-xl shadow-primary-500/20 flex items-center justify-center gap-3 hover:scale-[1.02]"
          >
            <Home size={20} />
            Back to Dashboard
          </button>
          
          <button 
            onClick={() => navigate('/messages')}
            className="w-full bg-white border border-slate-200 text-black p-5 rounded-3xl font-bold transition-all hover:bg-slate-100 flex items-center justify-center gap-3"
          >
            <MessageCircle size={20} />
            Congratulate Roommates
            <ArrowRight size={18} className="text-slate-400" />
          </button>
        </div>
      </div>
    </div>
  );
};

export default BookingSuccessScreen;
