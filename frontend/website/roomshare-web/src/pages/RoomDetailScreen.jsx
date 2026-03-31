import React from 'react';
import { motion } from 'framer-motion';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, MapPin, CheckCircle, Wifi, Droplets, Dumbbell, Car } from 'lucide-react';

const RoomDetailScreen = () => {
  const { id } = useParams();

  return (
    <div className="p-4 md:p-8 pb-32">
      {/* Top Bar */}
      <div className="flex items-center gap-4 mb-6">
        <Link to="/home" className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors">
          <ArrowLeft size={20} className="text-black" />
        </Link>
        <h1 className="text-2xl font-bold font-heading text-black">Room Details</h1>
      </div>

      {/* Hero Image */}
      <motion.div 
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        className="w-full h-64 md:h-96 rounded-3xl overflow-hidden mb-8 relative"
      >
        <img 
          src="https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1000&q=80" 
          alt="Room" 
          className="w-full h-full object-cover"
        />
        <div className="absolute top-4 right-4 bg-white/80 backdrop-blur-md px-4 py-2 rounded-xl border border-slate-200 shadow-lg">
          <span className="text-primary-700 font-bold text-lg">$1,200</span>
          <span className="text-slate-600 text-sm font-medium"> / month</span>
        </div>
      </motion.div>

      <div className="grid md:grid-cols-3 gap-8">
        {/* Main Details */}
        <div className="md:col-span-2">
          <motion.div 
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="mb-8"
          >
            <h2 className="text-4xl font-bold font-heading mb-2 text-black">Luxury Appt near Downtown</h2>
            <div className="flex items-center gap-2 text-slate-600 mb-6 font-medium">
              <MapPin size={18} className="text-primary-600" />
              <span>Manhattan, NY</span>
            </div>
            
            <h3 className="text-xl font-bold mb-4 text-black">About the room</h3>
            <p className="text-black leading-relaxed mb-6 font-medium">
              Stunning apartment with sweeping city views. You will have your own private bedroom and bathroom. The living space and kitchen are fully furnished. Building includes a gym and rooftop pool.
            </p>

            <h3 className="text-xl font-bold mb-4">Amenities</h3>
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
              {[
                { icon: Wifi, label: 'High-Speed WiFi' },
                { icon: CheckCircle, label: 'Private Bath' },
                { icon: Dumbbell, label: 'Gym Access' },
                { icon: Droplets, label: 'In-unit Laundry' },
                { icon: Car, label: 'Parking' },
              ].map((amenity, i) => (
                <div key={i} className="glass-panel p-4 rounded-2xl flex flex-col items-center justify-center text-center gap-2 border-slate-200">
                  <amenity.icon size={24} className="text-primary-600" />
                  <span className="text-xs font-bold text-slate-700">{amenity.label}</span>
                </div>
              ))}
            </div>
          </motion.div>
        </div>

        {/* Sidebar Booking Card */}
        <div>
          <motion.div 
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            className="glass-panel p-6 rounded-3xl sticky top-24 border border-slate-200 shadow-xl"
          >
            <h3 className="text-xl font-bold mb-4 text-black">Are you interested?</h3>
            <div className="flex items-center gap-4 mb-6 p-4 bg-slate-50 rounded-2xl border border-slate-200">
              <div className="w-12 h-12 rounded-full overflow-hidden border-2 border-primary-600">
                <img src="https://images.unsplash.com/photo-1599566150163-29194dcaad36?w=200&q=80" alt="Host" className="w-full h-full object-cover" />
              </div>
              <div>
                <p className="font-bold text-black">Jordan Lee</p>
                <p className="text-xs text-slate-600 font-bold">Host • AI Match 98%</p>
              </div>
            </div>

            <Link 
              to={`/room/${id || 1}/book`}
              className="w-full bg-primary-600 hover:bg-primary-500 text-white font-bold py-4 rounded-xl flex items-center justify-center transition-transform hover:scale-105 shadow-lg shadow-primary-500/30"
            >
              Book Room Now
            </Link>
          </motion.div>
        </div>
      </div>
    </div>
  );
};

export default RoomDetailScreen;
