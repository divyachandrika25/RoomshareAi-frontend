import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, MessageSquare, Check, X, 
  Home, Calendar, Clock, User, 
  ShieldCheck, Sparkles, ChevronRight,
  MapPin, Briefcase, Mail
} from 'lucide-react';

const RequestDetailScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const [status, setStatus] = useState('PENDING');

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 800);
  }, []);

  const request = {
    requesterName: "Mike Chen",
    requesterPhoto: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=400",
    requesterEmail: "mike.chen@techmail.com",
    roomTitle: "Luxury Master Loft - Pacific Heights",
    introMessage: "I've been looking for a place in this area for months. Your lifestyle match score for us is 98%, and I think we'd vibe really well. I'm a clean, quiet tenant who works in AI research.",
    moveInDate: "Oct 15, 2024",
    duration: "12 Months",
    employment: "Full-time (Lead Researcher)"
  };

  const handleAction = (newStatus) => {
    setStatus(newStatus);
    // Logic to update backend
  };

  return (
    <div className="bg-slate-50 min-h-screen pb-32">
       {/* Header */}
       <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-10 flex items-center justify-between">
          <div className="flex items-center gap-4">
             <button 
              onClick={() => navigate(-1)}
              className="w-10 h-10 bg-slate-50 rounded-full flex items-center justify-center hover:bg-slate-100 transition-colors"
            >
              <ArrowLeft size={20} className="text-black" />
            </button>
            <h1 className="text-lg font-black text-black tracking-tight">Request Detail</h1>
          </div>
          <div className="w-10" />
       </div>

       <div className="max-w-2xl mx-auto p-6 space-y-8">
          {isLoading ? (
             <div className="py-32 flex flex-col items-center justify-center gap-4">
                <div className="w-10 h-10 border-4 border-primary-600 border-t-transparent rounded-full animate-spin" />
                <p className="text-xs font-black text-slate-400 uppercase tracking-widest">Loading Neural Profile...</p>
             </div>
          ) : (
             <motion.div 
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              className="space-y-8"
             >
                {/* Requester Profile Card */}
                <div className="bg-white p-8 rounded-[40px] border border-slate-100 shadow-sm text-center">
                   <div className="relative inline-block mb-6">
                      <img src={request.requesterPhoto} alt={request.requesterName} className="w-32 h-32 rounded-[48px] object-cover shadow-2xl border-4 border-white" />
                      <div className="absolute -bottom-2 -right-2 bg-primary-600 text-white p-2.5 rounded-2xl shadow-lg border-2 border-white">
                         <ShieldCheck size={20} />
                      </div>
                   </div>
                   <h2 className="text-2xl font-black text-black tracking-tight uppercase mb-1">{request.requesterName}</h2>
                   <div className="flex items-center justify-center gap-2 mb-6">
                      <Mail size={14} className="text-slate-300" />
                      <span className="text-xs font-bold text-slate-400">{request.requesterEmail}</span>
                   </div>

                   <button 
                    onClick={() => navigate(`/chat/${id}`)}
                    className="w-full bg-primary-50 hover:bg-primary-100 py-4 rounded-2xl flex items-center justify-center gap-3 text-primary-600 transition-all group font-black uppercase text-xs tracking-widest"
                   >
                      <MessageSquare size={18} className="group-hover:scale-110 transition-transform" />
                      Send Private Message
                   </button>
                </div>

                {/* Listing Overview */}
                <div className="space-y-4">
                   <h3 className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] px-2">Listing Reference</h3>
                   <div className="bg-white p-5 rounded-[32px] border border-slate-100 shadow-sm flex items-center gap-4">
                      <div className="w-12 h-12 bg-primary-50 text-primary-600 rounded-2xl flex items-center justify-center">
                         <Home size={22} />
                      </div>
                      <div className="flex-1 min-w-0">
                         <h4 className="font-black text-black truncate uppercase text-sm tracking-tight">{request.roomTitle}</h4>
                         <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Room Listing ID: #RS-9281</p>
                      </div>
                   </div>
                </div>

                {/* Intro Message */}
                <div className="space-y-4">
                   <h3 className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] px-2">Introduction</h3>
                   <div className="bg-slate-900 p-8 rounded-[40px] text-white relative overflow-hidden group">
                      <div className="absolute top-0 right-0 p-6 opacity-10 group-hover:opacity-20 transition-opacity">
                         <Sparkles size={64} className="text-primary-400" />
                      </div>
                      <p className="text-sm font-bold leading-relaxed italic relative z-10">
                        "{request.introMessage}"
                      </p>
                   </div>
                </div>

                {/* Details Grid */}
                <div className="space-y-4">
                   <h3 className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] px-2">Proposed Terms</h3>
                   <div className="bg-white rounded-[40px] border border-slate-100 overflow-hidden shadow-sm divide-y divide-slate-50">
                      {[
                        { icon: Calendar, label: "Move-in Date", value: request.moveInDate },
                        { icon: Clock, label: "Stay Duration", value: request.duration },
                        { icon: Briefcase, label: "Employment", value: request.employment }
                      ].map((detail, i) => (
                        <div key={i} className="p-6 flex items-center gap-5">
                           <div className="w-10 h-10 bg-slate-50 text-slate-400 rounded-xl flex items-center justify-center">
                              <detail.icon size={20} />
                           </div>
                           <div>
                              <p className="text-[8px] font-black text-slate-300 uppercase tracking-widest">{detail.label}</p>
                              <p className="text-sm font-black text-slate-700 uppercase">{detail.value}</p>
                           </div>
                        </div>
                      ))}
                   </div>
                </div>

                {/* Action Footer */}
                <div className="sticky bottom-8 left-0 right-0 pt-8">
                   <AnimatePresence mode="wait">
                      {status === 'PENDING' ? (
                        <div className="flex gap-4">
                           <button 
                            onClick={() => handleAction('DECLINED')}
                            className="flex-1 bg-white border-2 border-red-50 text-red-600 py-6 rounded-[28px] font-black uppercase text-sm tracking-widest hover:bg-red-50 transition-all shadow-xl shadow-red-500/5 active:scale-95"
                           >
                             Decline
                           </button>
                           <button 
                            onClick={() => handleAction('ACCEPTED')}
                            className="flex-1 bg-green-500 text-white py-6 rounded-[28px] font-black uppercase text-sm tracking-widest hover:bg-green-600 transition-all shadow-xl shadow-green-500/20 active:scale-95 flex items-center justify-center gap-2"
                           >
                             <Check size={20} strokeWidth={3} />
                             Accept
                           </button>
                        </div>
                      ) : (
                        <motion.div
                          initial={{ opacity: 0, scale: 0.9 }}
                          animate={{ opacity: 1, scale: 1 }}
                          className={`w-full py-6 rounded-[28px] font-black uppercase text-sm tracking-widest text-center shadow-xl ${status === 'ACCEPTED' ? 'bg-green-50 text-green-600 border border-green-100' : 'bg-red-50 text-red-600 border border-red-100'}`}
                        >
                          {status === 'ACCEPTED' ? '✓ Request Accepted' : '✕ Request Declined'}
                        </motion.div>
                      )}
                   </AnimatePresence>
                </div>
             </motion.div>
          )}
       </div>
    </div>
  );
};

export default RequestDetailScreen;
