import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  Sun, Moon, Coffee, Dumbbell, Book, Music, 
  Sparkles, ArrowRight, Loader2, Check, X,
  Clock, Volume2, Shield
} from 'lucide-react';

const LifestyleHabitsScreen = () => {
  const [selectedHabits, setSelectedHabits] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const habitsList = [
    { id: 'early_bird', icon: Sun, label: 'Early Bird', category: 'Schedule' },
    { id: 'night_owl', icon: Moon, label: 'Night Owl', category: 'Schedule' },
    { id: 'fitness', icon: Dumbbell, label: 'Fitness Junkie', category: 'Lifestyle' },
    { id: 'coffee', icon: Coffee, label: 'Coffee Obsessed', category: 'Lifestyle' },
    { id: 'reader', icon: Book, label: 'Quiet Reader', category: 'Hobbies' },
    { id: 'music', icon: Music, label: 'Music Lover', category: 'Hobbies' },
    { id: 'remote', icon: Clock, label: 'Work from Home', category: 'Work' },
    { id: 'social', icon: Volume2, label: 'Very Social', category: 'Lifestyle' },
    { id: 'tidy', icon: Shield, label: 'Neat Freak', category: 'Hygiene' },
  ];

  const toggleHabit = (id) => {
    if (selectedHabits.includes(id)) setSelectedHabits(prev => prev.filter(h => h !== id));
    else setSelectedHabits(prev => [...prev, id]);
  };

  const handleComplete = () => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate('/home');
    }, 2000);
  };

  return (
    <div className="p-8 pb-24 md:pb-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[50%] h-[50%] bg-primary-500/10 blur-[100px] rounded-full" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[50%] h-[50%] bg-purple-500/10 blur-[100px] rounded-full" />

      <div className="glass-panel p-8 md:p-12 rounded-3xl w-full max-w-2xl relative z-10 border border-slate-200 shadow-2xl">
        <div className="text-center mb-10">
          <div className="inline-flex items-center gap-2 bg-purple-50 border border-purple-200 text-purple-700 px-4 py-2 rounded-full mb-6 text-sm font-bold">
            <Sparkles size={16} />
            <span>AI Matching Engine</span>
          </div>
          <h1 className="text-4xl font-bold font-heading mb-3 text-black">Your Lifestyle</h1>
          <p className="text-slate-600 font-medium max-w-md mx-auto">These habits help our AI find people you'll actually enjoy living with.</p>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-10">
          {habitsList.map((habit, index) => {
            const isSelected = selectedHabits.includes(habit.id);
            return (
              <motion.button
                key={habit.id}
                initial={{ opacity: 0, scale: 0.9 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: index * 0.05 }}
                type="button"
                onClick={() => toggleHabit(habit.id)}
                className={`group p-5 rounded-2xl border-2 transition-all flex flex-col gap-3 relative overflow-hidden ${
                  isSelected 
                    ? 'bg-primary-600 border-primary-700 text-white shadow-xl scale-[1.02]'
                    : 'bg-white border-slate-100 text-slate-700 hover:border-primary-200 hover:bg-slate-50'
                }`}
              >
                <div className={`w-10 h-10 rounded-xl flex items-center justify-center ${
                  isSelected ? 'bg-white/20' : 'bg-slate-100 group-hover:bg-primary-50'
                }`}>
                  <habit.icon size={20} className={isSelected ? 'text-white' : 'text-slate-600 group-hover:text-primary-600'} />
                </div>
                <div>
                  <p className="text-xs font-bold opacity-60 uppercase tracking-tighter mb-0.5">{habit.category}</p>
                  <p className="text-sm font-bold">{habit.label}</p>
                </div>
                {isSelected && (
                  <div className="absolute top-3 right-3 bg-white text-primary-600 rounded-full p-0.5">
                    <Check size={12} strokeWidth={4} />
                  </div>
                )}
              </motion.button>
            )
          })}
        </div>

        <div className="flex flex-col sm:flex-row gap-4">
          <button 
            onClick={() => navigate('/home')}
            className="flex-1 bg-slate-100 hover:bg-slate-200 text-black p-4 rounded-2xl font-bold transition-all"
          >
            Skip for now
          </button>
          <button 
            onClick={handleComplete}
            disabled={isLoading || selectedHabits.length === 0}
            className="flex-[2] bg-gradient-to-r from-primary-600 to-primary-500 hover:from-primary-500 hover:to-primary-400 text-white p-4 rounded-2xl font-bold transition-all shadow-lg shadow-primary-500/25 flex items-center justify-center gap-2 group disabled:opacity-70 disabled:grayscale"
          >
            {isLoading ? <Loader2 size={20} className="animate-spin" /> : (
              <>
                Confirm Selections
                <ArrowRight size={18} className="group-hover:translate-x-1 transition-transform" />
              </>
            )}
          </button>
        </div>

        <p className="text-center text-xs text-slate-400 mt-8 font-medium">
          Our AI uses these data points to calculate the compatibility scores you see on rooms.
        </p>
      </div>
    </div>
  );
};

export default LifestyleHabitsScreen;
