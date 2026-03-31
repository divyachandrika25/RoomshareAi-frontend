import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, Calendar, Clock, MapPin, 
  CheckCircle2, Info, ChevronRight 
} from 'lucide-react';

const ScheduleTourScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [selectedDate, setSelectedDate] = useState('Oct 25');
  const [selectedTime, setSelectedTime] = useState('10:00 AM');
  const [isLoading, setIsLoading] = useState(false);

  const dates = [
    { label: 'Oct 25', day: 'Fri' },
    { label: 'Oct 26', day: 'Sat' },
    { label: 'Oct 27', day: 'Sun' },
    { label: 'Oct 28', day: 'Mon' },
    { label: 'Oct 29', day: 'Tue' }
  ];

  const times = [
    '09:00 AM', '10:00 AM', '11:30 AM', 
    '01:00 PM', '02:00 PM', '04:30 PM'
  ];

  const handleConfirm = () => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate('/tour-success');
    }, 1500);
  };

  return (
    <div className="bg-slate-50 min-h-screen pb-32">
      <div className="bg-white px-6 py-6 border-b border-slate-200 flex items-center justify-between sticky top-0 z-30">
        <button 
          onClick={() => navigate(-1)}
          className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
        >
          <ArrowLeft size={20} className="text-black" />
        </button>
        <h1 className="text-lg font-bold text-black">Schedule Room Tour</h1>
        <div className="w-10" />
      </div>

      <div className="max-w-2xl mx-auto p-6 space-y-8">
        {/* Property Context */}
        <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm flex items-center gap-4">
          <div className="w-16 h-16 bg-primary-100 rounded-2xl flex items-center justify-center text-primary-600 shrink-0">
            <MapPin size={24} />
          </div>
          <div>
            <h3 className="font-bold text-black text-lg">Pacific Heights Loft</h3>
            <p className="text-sm text-slate-500 font-medium">2450 Washington St, San Francisco</p>
          </div>
        </div>

        {/* Date Selection */}
        <div className="space-y-4">
          <div className="flex justify-between items-end px-2">
            <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest">Select Date</h3>
            <span className="text-xs font-bold text-primary-600">October 2024</span>
          </div>
          <div className="flex gap-3 overflow-x-auto pb-4 no-scrollbar">
            {dates.map((date) => {
              const isActive = selectedDate === date.label;
              return (
                <button
                  key={date.label}
                  onClick={() => setSelectedDate(date.label)}
                  className={`min-w-[80px] p-4 rounded-2xl border-2 flex flex-col items-center gap-1 transition-all ${
                    isActive 
                      ? 'bg-primary-600 border-primary-700 text-white shadow-lg' 
                      : 'bg-white border-slate-100 text-slate-600 hover:border-primary-100'
                  }`}
                >
                  <span className={`text-[10px] font-black uppercase tracking-tighter ${isActive ? 'text-primary-100' : 'text-slate-400'}`}>
                    {date.day}
                  </span>
                  <span className="text-lg font-black">{date.label.split(' ')[1]}</span>
                </button>
              );
            })}
          </div>
        </div>

        {/* Time Selection */}
        <div className="space-y-4">
          <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest px-2">Select Time</h3>
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
            {times.map((time) => {
              const isActive = selectedTime === time;
              return (
                <button
                  key={time}
                  onClick={() => setSelectedTime(time)}
                  className={`p-4 rounded-2xl border-2 font-bold transition-all text-sm ${
                    isActive 
                      ? 'bg-primary-600 border-primary-700 text-white shadow-lg' 
                      : 'bg-white border-slate-100 text-slate-600 hover:border-primary-100'
                  }`}
                >
                  {time}
                </button>
              );
            })}
          </div>
        </div>

        {/* Notice Card */}
        <div className="bg-primary-50 border border-primary-200 p-6 rounded-3xl flex gap-4 items-start">
          <div className="w-10 h-10 bg-white rounded-xl flex items-center justify-center text-primary-600 shrink-0 shadow-sm">
            <Info size={18} />
          </div>
          <p className="text-sm text-primary-900 font-medium leading-relaxed">
            Tours typically last 30 minutes. Please arrive 5 minutes early. Your roommate group has been notified of this request.
          </p>
        </div>
      </div>

      {/* Footer Action */}
      <div className="fixed bottom-0 left-0 w-full bg-white border-t border-slate-200 p-6 z-40">
        <div className="max-w-2xl mx-auto">
          <button 
            onClick={handleConfirm}
            disabled={isLoading}
            className="w-full bg-primary-600 hover:bg-primary-700 text-white p-5 rounded-2xl font-bold transition-all shadow-xl shadow-primary-500/20 flex items-center justify-center gap-3 hover:scale-[1.02] disabled:opacity-70"
          >
            {isLoading ? <motion.div animate={{ rotate: 360 }} transition={{ repeat: Infinity, duration: 1 }} className="w-5 h-5 border-2 border-white border-t-transparent rounded-full" /> : (
              <>
                <Calendar size={20} />
                Confirm Schedule
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ScheduleTourScreen;
