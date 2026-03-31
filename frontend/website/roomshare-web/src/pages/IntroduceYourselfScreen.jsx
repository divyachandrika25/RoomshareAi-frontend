import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { User, ArrowRight, Loader2, Sparkles, Camera } from 'lucide-react';
import { motion } from 'framer-motion';

const IntroduceYourselfScreen = () => {
  const [bio, setBio] = useState('');
  const [occupation, setOccupation] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleComplete = (e) => {
    e.preventDefault();
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate('/lifestyle-habits');
    }, 1500);
  };

  return (
    <div className="p-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary-500/10 blur-[100px] rounded-full" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-purple-500/10 blur-[100px] rounded-full" />

      <div className="glass-panel p-8 md:p-10 rounded-3xl w-full max-w-lg relative z-10 border border-slate-200 shadow-2xl">
        <div className="text-center mb-8">
           <div className="inline-flex items-center gap-2 bg-primary-50 border border-primary-200 text-primary-700 px-4 py-2 rounded-full mb-6 text-sm font-bold">
            <Sparkles size={16} />
            <span>AI Personalization</span>
          </div>
          <h1 className="text-4xl font-bold font-heading mb-2 text-black">Who are you?</h1>
          <p className="text-slate-600 font-medium">Tell us and our AI a bit about yourself.</p>
        </div>

        <form onSubmit={handleComplete} className="space-y-6">
          <div className="flex flex-col items-center mb-8">
            <div className="w-24 h-24 bg-slate-100 rounded-full border-4 border-white shadow-lg flex items-center justify-center relative group cursor-pointer hover:bg-slate-200 transition-colors">
              <User size={40} className="text-slate-400" />
              <div className="absolute bottom-0 right-0 bg-primary-600 p-2 rounded-full text-white shadow-lg border-2 border-white translate-x-1 translate-y-1">
                <Camera size={14} />
              </div>
            </div>
            <p className="text-xs font-bold text-primary-600 mt-4 uppercase tracking-wider">Upload Profile Photo</p>
          </div>

          <div>
            <label className="text-sm font-bold text-slate-700 block mb-2 px-1">What do you do?</label>
            <input 
              type="text" 
              value={occupation}
              onChange={(e) => setOccupation(e.target.value)}
              placeholder="e.g. Software Engineer at Google"
              className="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 px-4 text-black font-bold outline-none focus:border-primary-500/50 shadow-sm"
            />
          </div>

          <div>
            <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Describe yourself</label>
            <textarea 
              rows="4"
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              placeholder="I'm a quiet person who loves reading and hiking... (AI will use this to match you!)"
              className="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 px-4 text-black font-bold outline-none focus:border-primary-500/50 shadow-sm resize-none"
            />
          </div>

          <button 
            type="submit" 
            disabled={isLoading}
            className="w-full bg-black text-white p-4 rounded-xl font-bold transition-all shadow-lg hover:bg-slate-800 flex items-center justify-center gap-2 group mt-4 hover:scale-[1.02]"
          >
            {isLoading ? <Loader2 size={20} className="animate-spin" /> : (
              <>
                Continue Processing
                <ArrowRight size={18} className="group-hover:translate-x-1 transition-transform" />
              </>
            )}
          </button>
        </form>
      </div>
    </div>
  );
};

export default IntroduceYourselfScreen;
