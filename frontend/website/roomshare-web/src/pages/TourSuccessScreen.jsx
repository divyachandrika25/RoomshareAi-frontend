import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  CheckCircle2, Calendar, MapPin, 
  MessageCircle, ArrowRight, Home, Info
} from 'lucide-react';

const TourSuccessScreen = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-white flex flex-col items-center justify-center p-8">
      <motion.div 
        initial={{ scale: 0 }}
        animate={{ scale: 1 }}
        transition={{ type: "spring", stiffness: 260, damping: 20 }}
        className="w-32 h-32 bg-green-500 rounded-full flex items-center justify-center mb-10 shadow-3xl shadow-green-500/30"
      >
        <Calendar size={64} className="text-white" />
      </motion.div>

      <div className="max-w-md w-full text-center space-y-6">
        <h1 className="text-4xl font-bold font-heading text-black">Tour Scheduled!</h1>
        <p className="text-slate-600 font-medium text-lg leading-relaxed px-4">
          Your tour for <span className="text-black font-bold">Pacific Heights Loft</span> has been confirmed. A notification has been sent to your group.
        </p>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="bg-slate-50 border border-slate-100 p-6 rounded-[32px] text-left space-y-5"
        >
          <div className="flex gap-4">
             <div className="w-10 h-10 bg-white rounded-xl flex items-center justify-center text-primary-600 shadow-sm border border-slate-100 shrink-0">
              <MapPin size={20} />
            </div>
            <div>
              <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Meeting Point</p>
              <p className="text-sm font-bold text-black">Main Entrance of the building</p>
            </div>
          </div>

          <div className="flex gap-4">
             <div className="w-10 h-10 bg-white rounded-xl flex items-center justify-center text-amber-500 shadow-sm border border-slate-100 shrink-0">
              <Info size={20} />
            </div>
            <div>
              <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Pro Tip</p>
              <p className="text-sm font-bold text-black leading-relaxed">Bring your ID and any questions you have for the landlord.</p>
            </div>
          </div>
        </motion.div>

        <div className="pt-8 space-y-4">
          <button 
            onClick={() => navigate('/messages')}
            className="w-full bg-primary-600 hover:bg-primary-700 text-white p-5 rounded-2xl font-bold transition-all shadow-xl shadow-primary-500/20 flex items-center justify-center gap-3 hover:scale-[1.02]"
          >
            <MessageCircle size={20} />
            Back to Group Chat
          </button>
          
          <button 
            onClick={() => navigate('/home')}
            className="w-full bg-white border border-slate-200 text-black p-5 rounded-2xl font-bold transition-all hover:bg-slate-50 flex items-center justify-center gap-3"
          >
            <Home size={20} />
            Go to Dashboard
          </button>
        </div>
      </div>
    </div>
  );
};

export default TourSuccessScreen;
