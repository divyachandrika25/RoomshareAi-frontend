import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, CheckCircle2, User, 
  MapPin, Calendar, Clock, Briefcase, 
  ShieldCheck, Info, Sparkles, ChevronRight
} from 'lucide-react';

const FinalReviewScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 900);
  }, []);

  const data = {
    title: "Review Request",
    subtitle: "Double check your group's details before sending the final request to the landlord.",
    roomTitle: "Pacific Heights Luxury Loft",
    ownerName: "Alexander Pierce",
    ownerPhoto: null,
    preferredMoveInDate: "Nov 01, 2024",
    durationOfStay: "12 Months (Fixed)",
    employmentStatus: "Full-time (Verified)",
    yourShareMonthly: 1200.00,
    groupSecurityDeposit: 3600.00,
    totalMoveIn: 4800.00,
    buttonText: "Send Final Request"
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-white">
        <motion.div 
          animate={{ rotate: 360 }}
          transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
          className="w-12 h-12 border-4 border-primary-500 border-t-transparent rounded-full"
        />
      </div>
    );
  }

  const DetailRow = ({ label, value, icon: Icon }) => (
    <div className="flex items-center justify-between py-4 border-b border-slate-50 last:border-0">
      <div className="flex items-center gap-3">
        {Icon && <div className="p-2 bg-slate-50 rounded-lg text-slate-400"><Icon size={14} /></div>}
        <span className="text-sm font-medium text-slate-500">{label}</span>
      </div>
      <span className="text-sm font-bold text-black">{value}</span>
    </div>
  );

  return (
    <div className="bg-slate-50 min-h-screen pb-32">
      {/* Header */}
      <div className="bg-white px-6 pt-6 pb-2 border-b border-slate-200 sticky top-0 z-40">
        <div className="flex items-center justify-between mb-6">
          <button 
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
          >
            <ArrowLeft size={20} className="text-black" />
          </button>
          <h1 className="text-lg font-bold text-black">Final Review</h1>
          <div className="w-10" />
        </div>
        
        {/* Step Progress */}
        <div className="flex gap-2 max-w-sm mx-auto mb-4">
          {[1,2,3,4].map(i => (
            <div key={i} className={`h-1.5 flex-1 rounded-full ${i === 4 ? 'bg-primary-600' : 'bg-primary-200'}`} />
          ))}
        </div>
      </div>

      <div className="max-w-2xl mx-auto p-6 space-y-8">
        <div className="pt-4">
          <h2 className="text-4xl font-black text-black font-heading tracking-tight mb-2">{data.title}</h2>
          <p className="text-slate-500 font-medium leading-relaxed">{data.subtitle}</p>
        </div>

        {/* Property Summary Card */}
        <motion.div 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          className="bg-white p-8 rounded-[40px] border border-slate-200 shadow-xl"
        >
          <div className="flex gap-6 items-center mb-8">
            <div className="w-24 h-24 bg-slate-200 rounded-3xl overflow-hidden shrink-0 shadow-lg">
              <img 
                src="https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=400" 
                alt="Room" 
                className="w-full h-full object-cover"
              />
            </div>
            <div>
              <h3 className="text-xl font-bold text-black mb-1">{data.roomTitle}</h3>
              <div className="flex items-center gap-2">
                 <div className="w-6 h-6 bg-slate-100 rounded-full flex items-center justify-center"><User size={12} className="text-slate-400" /></div>
                 <p className="text-xs font-black text-slate-400 uppercase tracking-widest">LANDLORD: {data.ownerName}</p>
              </div>
            </div>
          </div>

          <div className="space-y-1">
            <DetailRow label="Move-in Date" value={data.preferredMoveInDate} icon={Calendar} />
            <DetailRow label="Duration" value={data.durationOfStay} icon={Clock} />
            <DetailRow label="Status" value={data.employmentStatus} icon={Briefcase} />
          </div>

          <div className="mt-8 pt-8 border-t border-slate-100 space-y-4">
             <div className="flex justify-between items-center text-slate-500 font-medium">
               <span>Your Share (Monthly)</span>
               <span className="text-black font-bold">${data.yourShareMonthly.toLocaleString()}</span>
             </div>
             <div className="flex justify-between items-center text-slate-500 font-medium">
               <span>Group Deposit</span>
               <span className="text-black font-bold">${data.groupSecurityDeposit.toLocaleString()}</span>
             </div>
             
             <div className="mt-6 p-6 bg-primary-600 rounded-3xl flex justify-between items-center text-white shadow-lg shadow-primary-500/20">
                <div className="flex flex-col">
                  <span className="text-[10px] font-black uppercase tracking-widest opacity-70">Total Move-in Requirement</span>
                  <span className="text-2xl font-black">${data.totalMoveIn.toLocaleString()}</span>
                </div>
                <div className="w-12 h-12 bg-white/20 rounded-2xl flex items-center justify-center backdrop-blur-md">
                  <ShieldCheck size={28} />
                </div>
             </div>
          </div>
        </motion.div>

        {/* AI Trust Notice */}
        <div className="bg-slate-900 p-8 rounded-[40px] text-white overflow-hidden relative group">
          <div className="absolute top-0 right-0 w-32 h-32 bg-primary-500/20 blur-3xl" />
          <div className="flex items-center gap-3 mb-4">
            <Sparkles size={20} className="text-primary-400 animate-pulse" />
            <span className="text-xs font-black uppercase tracking-widest text-primary-400">AI Trust Engine</span>
          </div>
          <p className="text-lg font-bold leading-relaxed relative z-10">
            "We've pre-validated your income and credit. This increases your approval chance by 85% compared to other applicants."
          </p>
        </div>
      </div>

      {/* Footer CTA */}
      <div className="fixed bottom-0 left-0 w-full bg-white border-t border-slate-200 p-6 z-40">
        <div className="max-w-2xl mx-auto">
          <button 
            onClick={() => navigate('/request-sent')}
            className="w-full bg-primary-600 hover:bg-primary-700 text-white p-6 rounded-3xl font-black text-lg transition-all shadow-2xl shadow-primary-500/30 flex items-center justify-center gap-3 hover:scale-[1.02] active:scale-95"
          >
            {data.buttonText}
            <ChevronRight size={24} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default FinalReviewScreen;
