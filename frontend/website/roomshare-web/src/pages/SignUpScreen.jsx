import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Link, useNavigate } from 'react-router-dom';
import { User, Mail, Lock, ArrowRight, Loader2, MapPin, DollarSign, Coffee, Moon, Sun, Dumbbell, Book, Music } from 'lucide-react';

const SignUpScreen = () => {
  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState({
    name: '', email: '', password: '', location: '', budget: ''
  });
  const [selectedHabits, setSelectedHabits] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleNext = (e) => {
    e.preventDefault();
    if (step === 1 && formData.name && formData.email && formData.password) {
      setStep(2);
    } else if (step === 2 && formData.location && formData.budget) {
      setStep(3); // Go to habits
    } else if (step === 3) {
      handleRegister();
    }
  };

  const handleRegister = () => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate('/home');
    }, 2000);
  };

  const handleChange = (e) => setFormData({...formData, [e.target.name]: e.target.value});

  const habitsList = [
    { id: 'early_bird', icon: Sun, label: 'Early Bird' },
    { id: 'night_owl', icon: Moon, label: 'Night Owl' },
    { id: 'fitness', icon: Dumbbell, label: 'Fitness' },
    { id: 'coffee', icon: Coffee, label: 'Coffee Reg' },
    { id: 'reader', icon: Book, label: 'Reader' },
    { id: 'music', icon: Music, label: 'Music Lover' },
  ];

  const toggleHabit = (id) => {
    if (selectedHabits.includes(id)) setSelectedHabits(prev => prev.filter(h => h !== id));
    else setSelectedHabits(prev => [...prev, id]);
  };

  return (
    <div className="p-8 flex items-center justify-center min-h-screen relative overflow-hidden">
      <div className="absolute top-[-20%] left-[-10%] w-[50%] h-[50%] bg-primary-500/10 blur-[120px] rounded-full" />
      <div className="absolute bottom-[-20%] right-[-10%] w-[50%] h-[50%] bg-purple-500/10 blur-[120px] rounded-full" />

      <div className="glass-panel p-8 md:p-10 rounded-3xl w-full max-w-md relative z-10 border border-slate-200 shadow-2xl">
        <div className="mb-8">
          <div className="flex items-center gap-2 mb-6">
            <button 
              type="button"
              onClick={() => step > 1 ? setStep(step - 1) : navigate('/login')} 
              className="text-slate-500 hover:text-black transition-colors text-sm font-bold flex items-center gap-1"
            >
              &larr; Back to Login
            </button>
          </div>
          <h1 className="text-3xl font-bold font-heading mb-2 text-black">Create Account</h1>
          <p className="text-slate-600 font-medium">Join Roomshare AI today</p>
        </div>

        <div className="flex gap-2 mb-8">
          <div className={`h-1 flex-1 rounded-full ${step >= 1 ? 'bg-primary-600' : 'bg-slate-200'}`} />
          <div className={`h-1 flex-1 rounded-full ${step >= 2 ? 'bg-primary-600' : 'bg-slate-200'}`} />
          <div className={`h-1 flex-1 rounded-full ${step >= 3 ? 'bg-primary-600' : 'bg-slate-200'}`} />
        </div>

        <form onSubmit={handleNext} className="space-y-5">
          {step === 1 && (
            <motion.div initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} className="space-y-5">
              <div>
                <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Full Name</label>
                <div className="relative">
                  <User size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                  <input 
                    name="name" type="text" value={formData.name} onChange={handleChange} required placeholder="John Doe"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-12 pr-4 text-black placeholder-slate-400 outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                  />
                </div>
              </div>
              <div>
                <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Email Address</label>
                <div className="relative">
                  <Mail size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                  <input 
                    name="email" type="email" value={formData.email} onChange={handleChange} required placeholder="you@example.com"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-12 pr-4 text-black placeholder-slate-400 outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                  />
                </div>
              </div>
              <div>
                <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Password</label>
                <div className="relative">
                  <Lock size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                  <input 
                    name="password" type="password" value={formData.password} onChange={handleChange} required placeholder="••••••••"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-12 pr-4 text-black outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                  />
                </div>
              </div>
            </motion.div>
          )}

          {step === 2 && (
            <motion.div initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} className="space-y-5">
              <div className="bg-primary-50 border border-primary-200 p-4 rounded-xl text-sm text-primary-900 mb-6 font-medium">
                Tell us a little about what you're looking for to help our AI match you.
              </div>
              <div>
                <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Preferred Location</label>
                <div className="relative">
                  <MapPin size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                  <input 
                    name="location" type="text" value={formData.location} onChange={handleChange} required placeholder="e.g. Brooklyn, NY"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-12 pr-4 text-black placeholder-slate-400 outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                  />
                </div>
              </div>
              <div>
                <label className="text-sm font-bold text-slate-700 block mb-2 px-1">Monthly Budget Limit ($)</label>
                <div className="relative">
                  <DollarSign size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                  <input 
                    name="budget" type="number" value={formData.budget} onChange={handleChange} required placeholder="1500"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3.5 pl-12 pr-4 text-black placeholder-slate-400 outline-none focus:border-primary-500/50 focus:bg-white transition-all font-bold shadow-sm"
                  />
                </div>
              </div>
            </motion.div>
          )}

          {step === 3 && (
            <motion.div initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} className="space-y-5">
               <div className="bg-primary-50 border border-primary-200 p-4 rounded-xl text-sm text-primary-900 mb-4 font-medium">
                Select your lifestyle habits for better roommate matching!
               </div>

               <div className="grid grid-cols-2 gap-3 pb-4">
                  {habitsList.map(habit => (
                    <button
                      key={habit.id}
                      type="button"
                      onClick={() => toggleHabit(habit.id)}
                      className={`p-3 rounded-xl border flex items-center gap-3 transition-all ${
                        selectedHabits.includes(habit.id) 
                          ? 'bg-primary-600 border-primary-700 text-white shadow-lg'
                          : 'bg-slate-50 border-slate-200 text-slate-700 hover:bg-slate-100'
                      }`}
                    >
                      <habit.icon size={18} />
                      <span className="text-sm font-semibold">{habit.label}</span>
                    </button>
                  ))}
               </div>
            </motion.div>
          )}

          <button 
            type="submit" 
            disabled={isLoading}
            className="w-full bg-gradient-to-r from-primary-600 to-primary-500 hover:from-primary-500 hover:to-primary-400 text-white p-4 rounded-xl font-bold transition-all shadow-lg shadow-primary-500/30 flex items-center justify-center gap-2 mt-6"
          >
            {isLoading ? (
              <Loader2 size={20} className="animate-spin" />
            ) : (
              <React.Fragment>
                {step < 3 ? 'Continue' : 'Complete Registration'}
                <ArrowRight size={18} />
              </React.Fragment>
            )}
          </button>
        </form>

        {step === 1 && (
          <p className="text-center text-sm text-slate-600 mt-8 font-medium">
            Already have an account?{' '}
            <Link to="/login" className="text-primary-600 font-bold hover:text-primary-700 transition-colors">
              Log In
            </Link>
          </p>
        )}
      </div>
    </div>
  );
};

export default SignUpScreen;
