import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, Heart, Share2, Sparkles, 
  CheckCircle2, AlertTriangle, ShieldCheck,
  TrendingUp, Star, Info
} from 'lucide-react';

const AICompatibilityScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const data = {
    totalMatch: 98,
    headline: "The Harmonious Match",
    summary: "You and Mike have almost identical living habits, making you one of our highest-rated matches this month.",
    breakdown: [
      { id: 1, title: 'Lifestyle & Habits', score: 95, note: 'Both value clean communal spaces.' },
      { id: 2, title: 'Sleep Schedule', score: 100, note: 'Both are night owls (12 AM - 8 AM).' },
      { id: 3, title: 'Social Battery', score: 88, note: 'Both prefer occasional guests.' },
      { id: 4, title: 'Budget Alignment', score: 100, note: 'Exact budget match found.' }
    ],
    conflictDetection: {
      title: 'Minimal Conflict Risk',
      message: 'No significant personality or habit clashes detected by our AI engine.',
      isRisk: false
    }
  };

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Top Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 flex items-center justify-between sticky top-0 z-30">
        <button 
          onClick={() => navigate(-1)}
          className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
        >
          <ArrowLeft size={20} className="text-black" />
        </button>
        <h1 className="text-lg font-bold text-black">AI Compatibility</h1>
        <div className="flex gap-2">
          <button className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center text-slate-400 hover:text-red-500 transition-colors"><Heart size={18} /></button>
          <button className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center text-slate-400 hover:text-primary-600 transition-colors"><Share2 size={18} /></button>
        </div>
      </div>

      <div className="max-w-3xl mx-auto p-6 space-y-8">
        {/* Main Score Hero */}
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-white p-8 rounded-[40px] border border-slate-200 shadow-xl text-center relative overflow-hidden"
        >
          <div className="absolute top-0 right-0 p-6">
            <div className="bg-primary-50 text-primary-600 px-3 py-1 rounded-full text-[10px] font-black tracking-widest uppercase">Verified</div>
          </div>
          
          <div className="relative w-48 h-48 mx-auto mb-8">
            <svg className="w-full h-full transform -rotate-90">
              <circle
                cx="96"
                cy="96"
                r="88"
                fill="none"
                stroke="currentColor"
                strokeWidth="12"
                className="text-slate-100"
              />
              <motion.circle
                cx="96"
                cy="96"
                r="88"
                fill="none"
                stroke="currentColor"
                strokeWidth="12"
                strokeDasharray={2 * Math.PI * 88}
                initial={{ strokeDashoffset: 2 * Math.PI * 88 }}
                whileInView={{ strokeDashoffset: (2 * Math.PI * 88) * (1 - data.totalMatch / 100) }}
                transition={{ duration: 2, ease: "easeOut" }}
                className="text-primary-600"
              />
            </svg>
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <span className="text-5xl font-black text-black">{data.totalMatch}%</span>
              <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Global Match</span>
            </div>
          </div>

          <h2 className="text-3xl font-bold mb-3 text-black">"{data.headline}"</h2>
          <p className="text-slate-600 font-medium leading-relaxed max-w-md mx-auto">{data.summary}</p>
        </motion.div>

        {/* Progress Breakdown */}
        <div className="grid gap-4">
          <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest px-2">Detailed Breakdown</h3>
          {data.breakdown.map((item, i) => (
            <motion.div 
              key={item.id}
              initial={{ opacity: 0, x: -20 }}
              whileInView={{ opacity: 1, x: 0 }}
              transition={{ delay: i * 0.1 }}
              viewport={{ once: true }}
              className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm"
            >
              <div className="flex justify-between items-center mb-4">
                <span className="font-bold text-black">{item.title}</span>
                <span className="font-black text-primary-600">{item.score}%</span>
              </div>
              <div className="h-2 bg-slate-100 rounded-full overflow-hidden mb-3">
                <motion.div 
                  initial={{ width: 0 }}
                  whileInView={{ width: `${item.score}%` }}
                  transition={{ duration: 1.5, delay: 0.5 }}
                  className="h-full bg-primary-600 rounded-full"
                />
              </div>
              <p className="text-xs text-slate-500 font-bold italic">"{item.note}"</p>
            </motion.div>
          ))}
        </div>

        {/* Conflict Detection Card */}
        <motion.div 
          initial={{ opacity: 0, scale: 0.95 }}
          whileInView={{ opacity: 1, scale: 1 }}
          viewport={{ once: true }}
          className={`p-1 rounded-[32px] bg-gradient-to-br ${data.conflictDetection.isRisk ? 'from-orange-400 to-red-500' : 'from-primary-400 to-purple-600 shadow-lg shadow-primary-500/20'}`}
        >
          <div className="bg-white p-8 rounded-[31px]">
            <div className="flex items-center gap-4 mb-6">
              <div className={`w-12 h-12 rounded-2xl flex items-center justify-center ${data.conflictDetection.isRisk ? 'bg-orange-50 text-orange-600' : 'bg-primary-50 text-primary-600'}`}>
                {data.conflictDetection.isRisk ? <AlertTriangle size={24} /> : <ShieldCheck size={24} />}
              </div>
              <div>
                <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">AI Audit System</p>
                <h4 className="text-xl font-bold text-black">{data.conflictDetection.title}</h4>
              </div>
            </div>
            <p className="text-slate-600 font-medium leading-relaxed">{data.conflictDetection.message}</p>
          </div>
        </motion.div>

        {/* AI Insight */}
        <div className="bg-slate-900 p-8 rounded-3xl text-white relative overflow-hidden group">
          <div className="absolute top-0 right-0 w-32 h-32 bg-primary-600/20 blur-3xl group-hover:bg-primary-600/40 transition-colors" />
          <div className="flex items-center gap-3 mb-4">
            <Sparkles size={20} className="text-primary-400" />
            <span className="text-xs font-black uppercase tracking-widest text-primary-400">Smart Insight</span>
          </div>
          <p className="text-lg font-bold leading-relaxed relative z-10">
            "Both of you are highly organized professionals. Our data suggests a 94% chance of maintaining local harmony for over 12 months."
          </p>
        </div>

        <button 
          onClick={() => navigate('/messages')}
          className="w-full bg-primary-600 hover:bg-primary-700 text-white p-5 rounded-2xl font-bold transition-all shadow-xl shadow-primary-500/20 flex items-center justify-center gap-2"
        >
          Start a Conversation <Sparkles size={18} />
        </button>
      </div>
    </div>
  );
};

export default AICompatibilityScreen;
