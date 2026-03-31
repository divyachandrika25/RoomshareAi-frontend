import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, Calendar, MapPin, 
  Clock, MessageSquare, Shield, 
  HelpCircle, ChevronRight 
} from 'lucide-react';

const RoomTourDetailScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 800);
  }, []);

  const tourDetail = {
    roomTitle: "Pacific Heights Loft",
    status: "Confirmed",
    address: "2450 Washington St, San Francisco",
    selectedDate: "Oct 25, 2024",
    selectedTime: "10:00 AM",
    meetingPoint: "Main Entrance of the building",
    proTip: "Bring your ID and any questions you have for the landlord."
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
    <div className="bg-slate-50 min-h-screen pb-24">
      <div className="bg-white px-6 py-6 border-b border-slate-200 flex items-center justify-between sticky top-0 z-30">
        <button 
          onClick={() => navigate(-1)}
          className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
        >
          <ArrowLeft size={20} className="text-black" />
        </button>
        <h1 className="text-lg font-bold text-black">Tour Details</h1>
        <div className="w-10" />
      </div>

      <div className="max-w-2xl mx-auto p-6 space-y-8">
        <div className="text-center pt-8">
          <motion.div 
            initial={{ scale: 0.8 }}
            animate={{ scale: 1 }}
            className="w-24 h-24 bg-green-50 rounded-full flex items-center justify-center mx-auto mb-8 border border-green-100 shadow-inner"
          >
            <Calendar size={48} className="text-green-600" />
          </motion.div>
          <h2 className="text-3xl font-bold text-black mb-3">{tourDetail.roomTitle}</h2>
          <div className="inline-flex bg-primary-50 text-primary-600 px-4 py-1.5 rounded-full text-sm font-bold border border-primary-100 shadow-sm">
            {tourDetail.status}
          </div>
        </div>

        {/* Schedule Info */}
        <div className="bg-white p-6 rounded-[32px] border border-slate-200 shadow-sm space-y-6">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-primary-50 rounded-2xl flex items-center justify-center text-primary-600 shrink-0">
              <MapPin size={24} />
            </div>
            <div>
              <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Address</p>
              <p className="text-base font-bold text-black leading-tight">{tourDetail.address}</p>
            </div>
          </div>

          <div className="flex gap-4">
             <div className="flex-1 flex items-center gap-4">
              <div className="w-12 h-12 bg-primary-50 rounded-2xl flex items-center justify-center text-primary-600 shrink-0">
                <Calendar size={24} />
              </div>
              <div>
                <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Date</p>
                <p className="text-base font-bold text-black">{tourDetail.selectedDate}</p>
              </div>
            </div>
            <div className="flex-1 flex items-center gap-4">
              <div className="w-12 h-12 bg-primary-50 rounded-2xl flex items-center justify-center text-primary-600 shrink-0">
                <Clock size={24} />
              </div>
              <div>
                <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Time</p>
                <p className="text-base font-bold text-black">{tourDetail.selectedTime}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Additional Details */}
        <div className="bg-white p-8 rounded-[32px] border border-slate-200 shadow-sm space-y-8">
          <div>
            <h4 className="text-sm font-black text-slate-400 uppercase tracking-widest mb-4">MEETING POINT</h4>
            <p className="text-slate-700 font-medium leading-relaxed bg-slate-50 p-4 rounded-2xl border border-slate-100">
               {tourDetail.meetingPoint}
            </p>
          </div>

          <div>
            <h4 className="text-sm font-black text-slate-400 uppercase tracking-widest mb-4">PRO TIP</h4>
            <div className="flex gap-4 bg-amber-50 p-6 rounded-2xl border border-amber-100">
              <Star className="text-amber-500 shrink-0 mt-0.5" size={20} />
              <p className="text-sm text-amber-900 font-bold leading-relaxed">
                {tourDetail.proTip}
              </p>
            </div>
          </div>
        </div>

        <div className="space-y-4 pt-4">
          <button 
            onClick={() => navigate('/messages')}
            className="w-full bg-primary-600 hover:bg-primary-700 text-white p-5 rounded-2xl font-bold transition-all shadow-xl shadow-primary-500/20 flex items-center justify-center gap-3 hover:scale-[1.02]"
          >
            <MessageSquare size={20} />
            Message Group
          </button>
          
          <button className="w-full bg-white border border-slate-200 text-slate-500 p-5 rounded-2xl font-bold transition-all hover:text-red-500 hover:border-red-100 flex items-center justify-center gap-2 text-sm">
            Cancel Scheduling
          </button>
        </div>
      </div>
    </div>
  );
};

export default RoomTourDetailScreen;
