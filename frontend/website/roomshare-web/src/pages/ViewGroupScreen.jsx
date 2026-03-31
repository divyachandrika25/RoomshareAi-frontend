import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, MessageCircle, Users, Sparkles, 
  MapPin, Star, Shield, Info, CheckCircle2 
} from 'lucide-react';

const ViewGroupScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 1000);
  }, []);

  const groupData = {
    groupName: "The Urban Explorers",
    harmonyScore: 96,
    aiCompatibilityInsight: "This group is highly compatible due to shared interests in quiet weeknights and being active during weekends. Everyone in this group has verified income and identity.",
    targetLocation: { name: "Brooklyn, NY" },
    groupMembers: [
      { id: 1, fullName: "Sarah Jenkins", role: "Group Leader", photo: null, email: "sarah@example.com" },
      { id: 2, fullName: "Mike Chen", role: "Member", photo: null, email: "mike@example.com" },
      { id: 3, fullName: "Jessica Williams", role: "Member", photo: null, email: "jess@example.com" }
    ],
    groupChatEnabled: true
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

  return (
    <div className="bg-slate-50 min-h-screen pb-32">
      {/* Header Banner */}
      <div className="relative h-64 bg-gradient-to-tr from-primary-600 to-purple-700 overflow-hidden">
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-0 left-0 w-64 h-64 bg-white blur-3xl rounded-full -translate-x-1/2 -translate-y-1/2" />
          <div className="absolute bottom-0 right-0 w-96 h-96 bg-primary-400 blur-3xl rounded-full translate-x-1/3 translate-y-1/3" />
        </div>
        
        <div className="relative z-10 h-full flex flex-col items-center justify-center text-center p-6 mt-4">
           <button 
            onClick={() => navigate(-1)}
            className="absolute top-6 left-6 w-10 h-10 bg-white/20 backdrop-blur-md text-white rounded-full flex items-center justify-center hover:bg-white/30 transition-colors"
          >
            <ArrowLeft size={20} />
          </button>

          <motion.div 
            initial={{ scale: 0.8, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            className="w-20 h-20 bg-white/10 backdrop-blur-xl border border-white/20 rounded-3xl flex items-center justify-center mb-4 shadow-2xl"
          >
            <Users size={40} className="text-white" />
          </motion.div>
          <h1 className="text-3xl font-bold text-white mb-2">{groupData.groupName}</h1>
          <div className="bg-white/20 backdrop-blur-md px-4 py-1.5 rounded-full border border-white/30 flex items-center gap-2">
            <Sparkles size={14} className="text-primary-200" />
            <span className="text-white text-sm font-bold">{groupData.harmonyScore}% Harmony Score</span>
          </div>
        </div>
      </div>

      <div className="max-w-3xl mx-auto px-6 -mt-8 relative z-20 space-y-6">
        {/* AI Insight Card */}
        <motion.div 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          className="bg-white p-8 rounded-[32px] border border-slate-200 shadow-xl"
        >
          <div className="flex items-center gap-3 mb-4">
            <div className="w-10 h-10 bg-amber-50 rounded-xl flex items-center justify-center text-amber-500">
              <Star size={20} />
            </div>
            <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest">AI COMPATIBILITY INSIGHT</h3>
          </div>
          <p className="text-slate-600 font-medium leading-relaxed italic">
            "{groupData.aiCompatibilityInsight}"
          </p>
        </motion.div>

        {/* Members List */}
        <div className="space-y-4">
          <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest px-2">GROUP MEMBERS</h3>
          {groupData.groupMembers.map((member, i) => (
            <motion.div 
              key={member.id}
              initial={{ x: -20, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              transition={{ delay: i * 0.1 }}
              className="bg-white p-4 rounded-3xl border border-slate-200 shadow-sm flex items-center gap-4 group hover:border-primary-500/50 transition-all cursor-pointer"
            >
              <div className="w-14 h-14 bg-slate-100 rounded-full flex items-center justify-center text-slate-400 border-2 border-white shadow-md overflow-hidden shrink-0">
                <Users size={24} />
              </div>
              <div className="flex-1">
                <h4 className="font-bold text-black group-hover:text-primary-600 transition-colors">{member.fullName}</h4>
                <p className="text-xs text-slate-500 font-bold uppercase tracking-tighter">{member.role}</p>
              </div>
              <div className="flex items-center gap-3">
                <div className="px-3 py-1 bg-primary-50 text-primary-600 rounded-lg text-[10px] font-black uppercase tracking-widest border border-primary-100">Profile</div>
              </div>
            </motion.div>
          ))}
        </div>

        {/* Location Card */}
        <div className="bg-white p-8 rounded-[32px] border border-slate-200 shadow-sm">
          <div className="flex items-center gap-4 mb-6">
            <div className="w-12 h-12 bg-primary-50 rounded-2xl flex items-center justify-center text-primary-600">
              <MapPin size={24} />
            </div>
            <div>
              <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Target Location</p>
              <h4 className="text-xl font-bold text-black">{groupData.targetLocation.name}</h4>
            </div>
          </div>
          <p className="text-slate-600 font-medium leading-relaxed">
            This area has been identified as the optimal location based on the collective preferences and work/study locations of all group members.
          </p>
        </div>
      </div>

      {/* Floating Action Button */}
      {groupData.groupChatEnabled && (
        <div className="fixed bottom-8 left-1/2 -translate-x-1/2 w-full max-w-lg px-6 z-40">
          <button 
            onClick={() => navigate('/messages')}
            className="w-full bg-primary-600 hover:bg-primary-700 text-white p-5 rounded-2xl font-bold transition-all shadow-2xl shadow-primary-500/30 flex items-center justify-center gap-3 hover:scale-[1.02]"
          >
            <MessageCircle size={24} />
            Start Group Chat
          </button>
        </div>
      )}
    </div>
  );
};

export default ViewGroupScreen;
