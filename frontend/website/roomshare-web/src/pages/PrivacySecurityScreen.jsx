import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, ShieldCheck, Lock, 
  Eye, ShieldAlert, 
  Heart, Info, ChevronRight 
} from 'lucide-react';

const PrivacySecurityScreen = () => {
  const navigate = useNavigate();

  const SecurityCard = ({ icon: Icon, title, description, color = "text-primary-600", bg = "bg-primary-50" }) => (
    <motion.div 
      initial={{ opacity: 0, y: 10 }}
      whileInView={{ opacity: 1, y: 0 }}
      viewport={{ once: true }}
      className="bg-white p-6 rounded-[32px] border border-slate-200 shadow-sm space-y-4"
    >
      <div className="flex items-center gap-4">
        <div className={`w-12 h-12 ${bg} ${color} rounded-2xl flex items-center justify-center shrink-0`}>
          <Icon size={24} />
        </div>
        <h3 className="text-lg font-bold text-black">{title}</h3>
      </div>
      <p className="text-sm text-slate-500 font-medium leading-relaxed">
        {description}
      </p>
    </motion.div>
  );

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-40 flex items-center justify-between">
        <button 
          onClick={() => navigate(-1)}
          className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
        >
          <ArrowLeft size={20} className="text-black" />
        </button>
        <h1 className="text-lg font-bold text-black">Privacy & Security</h1>
        <div className="w-10" />
      </div>

      <div className="max-w-2xl mx-auto p-6 space-y-8">
        {/* Hero Badge */}
        <motion.div 
          initial={{ scale: 0.9, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          className="bg-green-50 p-8 rounded-[40px] border border-green-100 flex flex-col items-center text-center space-y-4"
        >
          <div className="w-16 h-16 bg-white rounded-2xl flex items-center justify-center text-green-500 shadow-sm border border-green-100">
            <ShieldCheck size={32} />
          </div>
          <div>
            <h2 className="text-xl font-black text-green-800 uppercase tracking-tight">Your account is secure</h2>
            <p className="text-green-700/70 text-sm font-bold">AI-driven protection is currently active</p>
          </div>
        </motion.div>

        <div className="space-y-6">
          <SecurityCard 
            icon={Lock}
            title="Data Protection"
            description="Your personal data and AI matching preferences are encrypted using industry-standard protocols. We never share your sensitive information with third parties."
          />

          <SecurityCard 
            icon={Eye}
            title="Profile Visibility"
            description="You control who sees your profile. By default, only verified potential roommates with a high harmony score can view your full details."
            color="text-purple-600"
            bg="bg-purple-50"
          />

          <SecurityCard 
            icon={ShieldAlert}
            title="AI Safe-Guard"
            description="Our AI engines continuously monitor for fraudulent listings and suspicious behavior to keep the Roomshare community safe."
            color="text-amber-500"
            bg="bg-amber-50"
          />
        </div>

        {/* AI Trust Engine Insight */}
        <div className="bg-slate-900 p-8 rounded-[40px] text-white relative overflow-hidden">
          <div className="absolute top-0 right-0 w-32 h-32 bg-primary-600/20 blur-3xl" />
          <div className="flex items-center gap-3 mb-4">
            <ShieldCheck size={20} className="text-primary-400" />
            <span className="text-[10px] font-black uppercase tracking-widest text-primary-400">Security Insight</span>
          </div>
          <p className="text-lg font-bold leading-relaxed relative z-10">
            "Your identity has been verified three times across our neural network, giving you a 'Platinum' trust status."
          </p>
        </div>

        <div className="text-center pt-8">
           <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">Last updated: October 2024</p>
           <button className="text-xs text-primary-600 font-bold hover:underline">View full Privacy Policy</button>
        </div>
      </div>
    </div>
  );
};

export default PrivacySecurityScreen;
