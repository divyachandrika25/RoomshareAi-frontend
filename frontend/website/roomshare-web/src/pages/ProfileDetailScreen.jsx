import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, MapPin, Heart, 
  Share2, Sparkles, MessageCircle, 
  Heart as HeartIcon, User, Work, 
  Calendar, Moon, Waves, Users,
  CheckCircle2, Star 
} from 'lucide-react';

const ProfileDetailScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const [isFavorite, setIsFavorite] = useState(false);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 1000);
  }, []);

  const data = {
    fullName: "Mike Chen",
    age: 26,
    photo: "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&q=80&w=400",
    roomStatus: "HAS ROOM",
    targetArea: "Pacific Heights, SF",
    matchPercentage: 98,
    aboutMe: "I'm a software engineer at a tech startup. Super clean, organized, and I usually spend my weekends hiking or exploring local coffee shops. Looking for someone chill who treats the apartment like a home, not just a place to sleep.",
    occupation: "Software Engineer",
    moveInDate: "Nov 01, 2024",
    sleepSchedule: "Night Owl",
    cleanliness: "Organized",
    socialInteraction: "Outgoing",
    budgetRange: "$1,800/mo"
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

  const HobbyCard = ({ icon: Icon, label, value }) => (
    <div className="bg-slate-50 p-4 rounded-2xl flex flex-col items-center text-center gap-1 border border-slate-100 hover:bg-white hover:border-primary-100 transition-all group">
      <div className="w-10 h-10 bg-white rounded-full flex items-center justify-center text-primary-500 shadow-sm border border-slate-50 group-hover:scale-110 transition-transform">
        <Icon size={18} />
      </div>
      <span className="text-[9px] font-black text-slate-400 uppercase tracking-widest mt-1">{label}</span>
      <span className="text-xs font-bold text-black">{value}</span>
    </div>
  );

  return (
    <div className="bg-white min-h-screen pb-32">
      {/* Hero Section */}
      <div className="relative h-[60vh] min-h-[400px]">
        <img src={data.photo} alt={data.fullName} className="w-full h-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-black/30" />
        
        {/* Navigation Overlays */}
        <div className="absolute top-0 left-0 w-full p-6 flex justify-between items-center z-20">
          <button 
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-white/20 backdrop-blur-md text-white rounded-full flex items-center justify-center border border-white/20"
          >
            <ArrowLeft size={20} />
          </button>
          
          <div className="flex gap-3">
             <button 
              onClick={() => setIsFavorite(!isFavorite)}
              className={`w-10 h-10 backdrop-blur-md rounded-full flex items-center justify-center border transition-all ${isFavorite ? 'bg-red-500 border-red-500 text-white' : 'bg-white/20 border-white/20 text-white'}`}
            >
              <HeartIcon size={20} fill={isFavorite ? "currentColor" : "none"} />
            </button>
            <button className="w-10 h-10 bg-white/20 backdrop-blur-md text-white rounded-full flex items-center justify-center border border-white/20">
              <Share2 size={20} />
            </button>
          </div>
        </div>

        {/* Identity Badge */}
        <div className="absolute top-24 left-6 z-20">
          <div className={`px-4 py-1.5 rounded-full text-[10px] font-black flex items-center gap-2 border backdrop-blur-md ${data.roomStatus === 'HAS ROOM' ? 'bg-green-500/20 text-green-400 border-green-500/30' : 'bg-primary-500/20 text-primary-400 border-primary-500/30'}`}>
            <CheckCircle2 size={12} />
            {data.roomStatus}
          </div>
        </div>

        {/* AI Score Badge */}
        <motion.div 
          initial={{ x: 20, opacity: 0 }}
          animate={{ x: 0, opacity: 1 }}
          className="absolute top-24 right-6 z-20 bg-amber-500 text-white px-4 py-2 rounded-2xl shadow-xl flex flex-col items-center border border-amber-400"
        >
          <span className="text-xl font-black">{data.matchPercentage}%</span>
          <span className="text-[8px] font-black uppercase tracking-widest -mt-1">Match</span>
        </motion.div>

        {/* Name Info Overlay */}
        <div className="absolute bottom-12 left-6 right-6 z-20 text-white">
          <h1 className="text-4xl font-black font-heading mb-2">{data.fullName}, {data.age}</h1>
          <div className="flex items-center gap-2 opacity-80">
            <MapPin size={16} />
            <p className="font-bold text-lg">{data.targetArea}</p>
          </div>
        </div>
      </div>

      <div className="max-w-3xl mx-auto px-6 -mt-8 relative z-30">
        <div className="bg-white rounded-[40px] p-8 shadow-2xl border border-slate-100 space-y-10">
          {/* Action Row */}
          <div className="flex gap-4">
            <button 
              onClick={() => navigate('/ai-compatibility')}
              className="flex-1 bg-slate-50 hover:bg-slate-100 p-5 rounded-3xl font-black text-primary-600 flex items-center justify-center gap-3 transition-all border border-slate-100 shadow-sm"
            >
              <Sparkles size={20} />
              AI Compatibility
            </button>
          </div>

          {/* About Section */}
          <div className="space-y-4">
            <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest">ABOUT {data.fullName.split(' ')[0]}</h3>
            <p className="text-slate-600 font-medium leading-relaxed text-lg italic">
              "{data.aboutMe}"
            </p>
          </div>

          {/* Quick Info */}
          <div className="grid grid-cols-2 gap-4">
             <div className="flex items-center gap-4 bg-slate-50 p-5 rounded-[32px] border border-slate-100">
                <div className="w-10 h-10 bg-white rounded-xl flex items-center justify-center text-primary-500 shadow-sm"><Work size={20} /></div>
                <div>
                  <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Occupation</p>
                  <p className="text-sm font-bold text-black">{data.occupation}</p>
                </div>
             </div>
             <div className="flex items-center gap-4 bg-slate-50 p-5 rounded-[32px] border border-slate-100">
                <div className="w-10 h-10 bg-white rounded-xl flex items-center justify-center text-primary-500 shadow-sm"><Calendar size={20} /></div>
                <div>
                  <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Move-in</p>
                  <p className="text-sm font-bold text-black">{data.moveInDate}</p>
                </div>
             </div>
          </div>

          {/* Lifestyle Habits */}
          <div className="space-y-4">
            <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest">LIFESTYLE HABITS</h3>
            <div className="grid grid-cols-3 gap-4">
              <HobbyCard icon={Moon} label="Schedule" value={data.sleepSchedule} />
              <HobbyCard icon={Waves} label="Clean" value={data.cleanliness} />
              <HobbyCard icon={Users} label="Social" value={data.socialInteraction} />
            </div>
          </div>
        </div>
      </div>

      {/* Floating Price & CTA */}
      <div className="fixed bottom-0 left-0 w-full bg-white border-t border-slate-100 p-6 z-40">
        <div className="max-w-3xl mx-auto flex items-center justify-between">
          <div className="flex flex-col">
            <span className="text-[10px] font-black text-slate-400 uppercase tracking-widest">MONTHLY BUDGET</span>
            <span className="text-2xl font-black text-primary-600">{data.budgetRange}</span>
          </div>
          <button 
            onClick={() => navigate('/messages')}
            className="bg-black text-white px-10 py-5 rounded-3xl font-black shadow-2xl flex items-center gap-3 hover:scale-105 active:scale-95 transition-all"
          >
            <MessageCircle size={24} />
            Message
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProfileDetailScreen;
