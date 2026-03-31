import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  CheckCircle2, Sparkles, Star, TrendingUp, 
  ArrowRight, Heart, MapPin, Zap, Bot
} from 'lucide-react';

const AIResultsScreen = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-white">
      {/* Dynamic Background */}
      <div className="fixed inset-0 pointer-events-none opacity-50">
        <div className="absolute top-0 left-0 w-full h-full bg-[radial-gradient(circle_at_50%_0%,#f0f7ff_0%,#ffffff_70%)]" />
      </div>

      <div className="relative z-10 p-8 max-w-4xl mx-auto pb-24">
        {/* Success Header */}
        <div className="text-center mb-16">
          <motion.div 
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            className="w-24 h-24 bg-green-500 rounded-full flex items-center justify-center mx-auto mb-8 shadow-2xl shadow-green-500/30"
          >
            <CheckCircle2 size={48} className="text-white" />
          </motion.div>
          <motion.h1 
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            className="text-4xl font-bold font-heading mb-4 text-black"
          >
            AI Matches <span className="text-primary-600">Generated</span>
          </motion.h1>
          <motion.p 
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
            className="text-slate-600 font-medium text-lg"
          >
            We've analyzed 50,000+ data points to find your top 3 perfect living situations.
          </motion.p>
        </div>

        {/* Result Cards */}
        <div className="grid gap-8 mb-16">
          {/* Main Result */}
          <motion.div 
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
            className="group glass-panel p-1 rounded-[40px] shadow-2xl shadow-primary-500/10 border border-primary-100 hover:border-primary-300 transition-all cursor-pointer"
            onClick={() => navigate('/home')}
          >
            <div className="bg-white rounded-[39px] p-8">
              <div className="flex flex-col md:flex-row gap-8 items-start md:items-center">
                <div className="w-full md:w-32 md:h-32 bg-primary-100 rounded-3xl flex flex-col items-center justify-center text-primary-600 shrink-0">
                  <span className="text-4xl font-black">98%</span>
                  <span className="text-[10px] font-bold uppercase tracking-widest mt-1">Match</span>
                </div>
                
                <div className="flex-1 space-y-3">
                  <div className="flex items-center gap-3">
                    <span className="bg-primary-50 text-primary-700 px-3 py-1 rounded-full text-xs font-black uppercase tracking-widest">Global Top Match</span>
                    <div className="flex text-yellow-500"><Star size={14} fill="currentColor" /><Star size={14} fill="currentColor" /><Star size={14} fill="currentColor" /></div>
                  </div>
                  <h3 className="text-2xl font-bold text-black">Luxury Loft in Downtown</h3>
                  <p className="text-slate-600 font-medium leading-relaxed">
                    Based on your "Night Owl" schedule and "Fitness" focus, this quiet professional community is optimized for your lifestyle.
                  </p>
                </div>

                <div className="w-full md:w-auto">
                  <button className="w-full md:w-16 h-16 bg-primary-600 rounded-2xl flex items-center justify-center text-white shadow-lg group-hover:scale-110 transition-transform">
                    <ArrowRight size={24} />
                  </button>
                </div>
              </div>
            </div>
          </motion.div>

          {/* Quick Metrics */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {[
              { icon: Zap, label: "Efficiency", value: "95%", color: "text-amber-500", bg: "bg-amber-50" },
              { icon: TrendingUp, label: "Price Confidence", value: "Great", color: "text-green-500", bg: "bg-green-50" },
              { icon: Heart, label: "Vibe Match", value: "Intense", color: "text-red-500", bg: "bg-red-50" }
            ].map((stat, i) => (
              <motion.div 
                key={i}
                initial={{ opacity: 0, scale: 0.9 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: 0.3 + (i * 0.1) }}
                className="bg-white border border-slate-100 p-6 rounded-3xl flex items-center gap-4 shadow-sm"
              >
                <div className={`w-12 h-12 rounded-2xl ${stat.bg} ${stat.color} flex items-center justify-center`}>
                  <stat.icon size={24} />
                </div>
                <div>
                  <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">{stat.label}</p>
                  <p className="text-lg font-bold text-black">{stat.value}</p>
                </div>
              </motion.div>
            ))}
          </div>
        </div>

        {/* Footer Insight */}
        <motion.div 
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.8 }}
          className="bg-slate-50 border border-slate-200 p-8 rounded-3xl flex flex-col md:flex-row items-center gap-6"
        >
          <div className="w-16 h-16 bg-white rounded-2xl flex items-center justify-center shadow-md shrink-0 border border-slate-100">
            <Bot size={32} className="text-primary-600" />
          </div>
          <div>
            <h4 className="text-black font-bold mb-1">Roomshare AI Assistant</h4>
            <p className="text-slate-600 text-sm font-medium">
              "I've pre-selected these rooms based on your budget of $1,200 and location in Manhattan. You can view all 15 filtered results on your home screen."
            </p>
          </div>
          <button 
            onClick={() => navigate('/home')}
            className="bg-black text-white px-8 py-4 rounded-full font-bold shadow-lg hover:shadow-black/20 transition-all hover:scale-105 shrink-0 whitespace-nowrap"
          >
            Open Dashboard
          </button>
        </motion.div>
      </div>
    </div>
  );
};

export default AIResultsScreen;
