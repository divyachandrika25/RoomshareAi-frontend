import { Outlet, Link, useLocation } from 'react-router-dom';
import { Home, Heart, MessageCircle, User, Bookmark } from 'lucide-react';
import { motion } from 'framer-motion';

const MainLayout = () => {
  const location = useLocation();

  const navItems = [
    { path: '/home', icon: Home, label: 'Home' },
    { path: '/matches', icon: Heart, label: 'Matches' },
    { path: '/saved-items', icon: Bookmark, label: 'Saved' },
    { path: '/messages', icon: MessageCircle, label: 'Inbox' },
    { path: '/profile', icon: User, label: 'Profile' },
  ];

  return (
    <div className="flex flex-col h-screen overflow-hidden">
      {/* Main Content Area */}
      <main className="flex-1 overflow-y-auto pb-20 md:pb-0 md:pl-20 relative">
        <div className="max-w-7xl mx-auto min-h-full">
          <Outlet />
        </div>
      </main>

      {/* Navigation (Bottom on Mobile, Left Sidebar on Desktop) */}
      <nav className="fixed bottom-0 w-full md:w-20 md:h-full md:left-0 md:bottom-auto bg-white/95 backdrop-blur-xl border-t md:border-t-0 md:border-r border-slate-200 z-50 shadow-[0_-4px_20px_rgba(0,0,0,0.05)] md:shadow-none">
        <div className="flex flex-row md:flex-col justify-around md:justify-center items-center h-16 md:h-full md:gap-8 px-4 md:px-0">
          {navItems.map((item) => {
            // Precise active matching to fix home bug
            const isActive = location.pathname === item.path || (item.path !== '/home' && location.pathname.startsWith(item.path));
            const Icon = item.icon;
            return (
              <Link 
                key={item.path} 
                to={item.path}
                className={`relative p-3 rounded-xl flex flex-col items-center gap-1 transition-all duration-300 ${
                  isActive ? 'text-primary-700' : 'text-slate-500 hover:text-black'
                }`}
              >
                {isActive && (
                  <motion.div
                    layoutId="nav-indicator"
                    className="absolute inset-0 bg-primary-50 rounded-xl border border-primary-100"
                    initial={false}
                    transition={{ type: "spring", stiffness: 300, damping: 30 }}
                  />
                )}
                <Icon size={24} className="relative z-10" />
                <span className="text-[10px] font-medium hidden md:block relative z-10">{item.label}</span>
              </Link>
            )
          })}
        </div>
      </nav>
    </div>
  );
};

export default MainLayout;
