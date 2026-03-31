import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, Search, Filter, 
  Sparkles, MapPin, Heart, 
  ChevronRight, Star, Globe, 
  Zap, ShieldCheck, UserPlus 
} from 'lucide-react';

const MatchesScreen = () => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 1200);
  }, []);

  const matches = [
    {
      id: 1,
      name: "Mike Chen",
      age: 26,
      photo: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=400",
      score: 98,
      reason: "High synergy with your 'Night Owl' schedule and cleanliness habits.",
      location: "San Francisco, CA",
      isPremium: true
    },
    {
      id: 2,
      name: "Sarah Jenkins",
      age: 24,
      photo: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=400",
      score: 94,
      reason: "Shared interest in 'Quiet Space' and similar budget range.",
      location: "Oakland, CA",
      isPremium: false
    },
    {
      id: 3,
      name: "David Smith",
      age: 28,
      photo: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=400",
      score: 89,
      reason: "Compatible work-from-home styles and social preferences.",
      location: "San Jose, CA",
      isPremium: false
    }
  ];

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-40">
        <div className="flex items-center justify-between mb-8">
          <div className="flex items-center gap-4">
             <button 
              onClick={() => navigate(-1)}
              className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
            >
              <ArrowLeft size={20} className="text-black" />
            </button>
             <div>
                <h1 className="text-2xl font-black text-black font-heading tracking-tight">AI Matches</h1>
                <p className="text-[10px] font-black text-primary-500 uppercase tracking-widest flex items-center gap-1">
                  <Zap size={10} fill="currentColor" /> Neural Network Active
                </p>
             </div>
          </div>
          <button className="w-10 h-10 bg-slate-50 border border-slate-100 rounded-full flex items-center justify-center text-slate-400 hover:text-primary-500 transition-colors shadow-sm">
            <Filter size={18} />
          </button>
        </div>

        {/* Search & Insight */}
        <div className="max-w-2xl mx-auto space-y-4">
           {/* AI Banner */}
           <div className="bg-slate-900 rounded-[32px] p-6 text-white overflow-hidden relative group">
              <div className="absolute inset-0 bg-gradient-to-r from-primary-600/20 to-transparent pointer-events-none" />
              <div className="relative z-10 flex items-center gap-4">
                 <div className="w-12 h-12 bg-white/10 backdrop-blur-xl rounded-2xl flex items-center justify-center border border-white/10 group-hover:scale-110 transition-transform">
                    <Sparkles size={24} className="text-primary-400" />
                 </div>
                 <div>
                    <h3 className="text-lg font-black tracking-tight mb-1">AI Smart Matching</h3>
                    <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Profiles analyzed across 12 dimensions</p>
                 </div>
              </div>
           </div>
        </div>
      </div>

      <div className="max-w-2xl mx-auto p-6 space-y-8">
        <div className="flex justify-between items-center">
           <h2 className="text-sm font-black text-slate-400 uppercase tracking-widest px-2">Your Top Picks</h2>
           <span className="text-[10px] font-black text-primary-600 uppercase tracking-widest bg-primary-50 px-3 py-1 rounded-full">3 matches found</span>
        </div>

        {isLoading ? (
          <div className="space-y-6">
            {[1,2,3].map(i => (
               <div key={i} className="h-32 bg-slate-100 rounded-[32px] animate-pulse" />
            ))}
          </div>
        ) : (
          <div className="space-y-6">
            <AnimatePresence>
              {matches.map((person, idx) => (
                <motion.div
                  key={person.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: idx * 0.1 }}
                  onClick={() => navigate(`/profile/${person.id}`)}
                  className="bg-white p-4 rounded-[40px] border border-slate-100 shadow-sm hover:shadow-2xl hover:border-primary-100 transition-all cursor-pointer group flex items-center gap-6"
                >
                  <div className="relative shrink-0">
                    <div className="w-24 h-24 rounded-[32px] overflow-hidden border-2 border-white shadow-xl">
                      <img src={person.photo} alt={person.name} className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700" />
                    </div>
                    <div className="absolute -top-1 -left-1 bg-amber-500 text-white px-2 py-1 rounded-lg text-[9px] font-black uppercase shadow-lg border border-amber-400">
                      {person.score}%
                    </div>
                    {person.isPremium && (
                      <div className="absolute -bottom-1 -right-1 bg-primary-600 text-white p-1.5 rounded-full border-2 border-white shadow-md">
                         <ShieldCheck size={12} />
                      </div>
                    )}
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex justify-between items-start mb-1">
                      <div>
                        <h3 className="text-lg font-black text-black group-hover:text-primary-600 transition-colors uppercase tracking-tight">{person.name}, {person.age}</h3>
                        <div className="flex items-center gap-1.5 text-slate-400">
                          <MapPin size={12} />
                          <span className="text-[10px] font-black uppercase tracking-widest">{person.location}</span>
                        </div>
                      </div>
                      <button className="w-10 h-10 rounded-full flex items-center justify-center text-slate-200 hover:text-red-500 hover:bg-red-50 transition-all">
                        <Heart size={20} />
                      </button>
                    </div>
                    
                    <p className="text-xs text-slate-500 font-medium leading-relaxed mt-3 line-clamp-2">
                      <Sparkles size={10} className="inline mr-1 text-primary-400" />
                      {person.reason}
                    </p>

                    <div className="flex items-center gap-4 mt-4">
                       <button className="flex items-center gap-1.5 text-[10px] font-black text-primary-600 uppercase tracking-widest hover:underline">
                        <UserPlus size={14} /> Send invite
                       </button>
                       <div className="w-1 h-1 bg-slate-200 rounded-full" />
                       <button className="flex items-center gap-1.5 text-[10px] font-black text-slate-400 uppercase tracking-widest hover:text-black">
                        View profile <ChevronRight size={14} />
                       </button>
                    </div>
                  </div>
                </motion.div>
              ))}
            </AnimatePresence>
          </div>
        )}

        <div className="pt-12 text-center pb-12">
           <div className="inline-flex items-center gap-3 bg-white px-6 py-3 rounded-full border border-slate-100 shadow-sm">
              <Globe size={14} className="text-primary-500 animate-spin-slow" />
              <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Watching for new compatibility matches in SF...</p>
           </div>
        </div>
      </div>
    </div>
  );
};

export default MatchesScreen;
