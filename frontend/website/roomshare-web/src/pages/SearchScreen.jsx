import React from 'react';
import { motion } from 'framer-motion';
import { ArrowLeft, Search, MapPin, DollarSign, SlidersHorizontal, Home } from 'lucide-react';
import { Link } from 'react-router-dom';

const SearchScreen = () => {
  return (
    <div className="p-4 md:p-8 pb-24 md:pb-12 min-h-screen">
      <div className="max-w-4xl mx-auto">
        <div className="flex items-center gap-4 mb-8">
          <Link to={-1} className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors border border-slate-200">
            <ArrowLeft size={20} className="text-black" />
          </Link>
          <h1 className="text-3xl font-bold font-heading text-black">Search & Filters</h1>
        </div>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="glass-panel p-6 md:p-10 rounded-3xl mb-8 border border-slate-200 shadow-xl"
        >
          <div className="relative mb-8">
            <Search size={20} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
            <input 
              type="text" 
              placeholder="Search by neighborhood, city, or zip code..."
              className="w-full bg-slate-50 border border-slate-200 rounded-2xl py-4 pl-12 pr-4 text-black font-bold outline-none focus:border-primary-500/50 focus:bg-white transition-all shadow-sm"
            />
          </div>

          <div className="grid md:grid-cols-2 gap-8">
            <div className="space-y-6">
              <h3 className="font-bold text-lg flex items-center gap-2 text-black">
                <DollarSign size={20} className="text-primary-600" /> Price Range
              </h3>
              <div className="flex items-center gap-4">
                <input type="number" placeholder="Min" className="w-full bg-slate-50 border border-slate-200 rounded-xl py-2 px-4 outline-none focus:border-primary-500/50 text-black font-bold" />
                <span className="text-slate-400 font-bold">-</span>
                <input type="number" placeholder="Max" className="w-full bg-slate-50 border border-slate-200 rounded-xl py-2 px-4 outline-none focus:border-primary-500/50 text-black font-bold" />
              </div>

              <h3 className="font-bold text-lg flex items-center gap-2 pt-4 text-black">
                <Home size={20} className="text-primary-600" /> Room Type
              </h3>
              <div className="flex flex-wrap gap-2">
                {['Private Room', 'Shared Room', 'Entire Place', 'Studio'].map(type => (
                  <button key={type} className="bg-slate-50 border border-slate-200 hover:border-primary-500 hover:bg-primary-50 px-4 py-2 rounded-full text-sm font-bold text-slate-700 transition-all">
                    {type}
                  </button>
                ))}
              </div>
            </div>

            <div className="space-y-6">
              <h3 className="font-bold text-lg flex items-center gap-2 text-black">
                <SlidersHorizontal size={20} className="text-primary-600" /> Amenities
              </h3>
              <div className="grid grid-cols-2 gap-3">
                {['Private Bath', 'Gym Access', 'In-Unit Laundry', 'Pet Friendly', 'Furnished', 'Parking'].map(amenity => (
                  <label key={amenity} className="flex items-center gap-3 cursor-pointer group">
                    <div className="w-5 h-5 rounded border border-slate-300 group-hover:border-primary-500 transition-all flex items-center justify-center bg-slate-50">
                      <input type="checkbox" className="hidden" />
                      {/* Check mark logic omitted for mock */}
                    </div>
                    <span className="text-slate-600 font-bold group-hover:text-black transition-colors">{amenity}</span>
                  </label>
                ))}
              </div>
            </div>
          </div>

          <div className="mt-10 flex gap-4">
            <button className="flex-1 glass-panel hover:bg-slate-100 py-4 rounded-xl font-bold transition-colors border border-slate-200 text-black">
              Reset Filters
            </button>
            <button className="flex-[2] bg-black hover:bg-slate-800 text-white font-bold py-4 rounded-xl transition-all shadow-lg">
              Apply Filters
            </button>
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default SearchScreen;
