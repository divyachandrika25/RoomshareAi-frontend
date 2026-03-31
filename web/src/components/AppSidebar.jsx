import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import {
  Home, Users, MessageCircle, User, Bell, Heart,
  LogOut, Sparkles, Settings, History, PlusCircle,
  ShieldCheck, Building2, Bot, Menu, X, CreditCard,Star
} from 'lucide-react';
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarSeparator,
  SidebarGroup,
  SidebarGroupLabel,
  SidebarGroupContent,
  SidebarTrigger,
  useSidebar,
} from "@/components/ui/sidebar";
import { cn } from "@/lib/utils";
import * as React from "react";

// ─── Nav Data ────────────────────────────────────────────────────────────────

const mainNav = [
  { to: '/dashboard', icon: Home, label: 'Home', end: true },
  { to: '/dashboard/matches', icon: Users, label: 'Matches' },
  { to: '/dashboard/messages', icon: MessageCircle, label: 'Messages' },
  { to: '/dashboard/ai-assistant', icon: Sparkles, label: 'AI Search', premium: true },
  { to: '/dashboard/hotels', icon: Building2, label: 'Hotels & Stays' },
  { to: '/dashboard/ai-chatbot', icon: Bot, label: 'AI Chatbot', premium: true },
];

const personalNav = [
  { to: '/dashboard/profile', icon: User, label: 'My Profile' },
  { to: '/dashboard/saved', icon: Heart, label: 'Saved Roommates' },
  { to: '/dashboard/booking-history', icon: History, label: 'Booking History' },
  { to: '/dashboard/notifications', icon: Bell, label: 'Notifications' },
];

const otherNav = [
  { to: '/dashboard/settings', icon: Settings, label: 'Settings' },
  { to: '/dashboard/privacy', icon: ShieldCheck, label: 'Privacy & Safety' },
  { to: '/dashboard/pricing', icon: CreditCard, label: 'Subscription' },
];

// ─── Mobile Top Bar ───────────────────────────────────────────────────────────

/**
 * Rendered only on mobile (md:hidden). Shows branding + hamburger toggle.
 * Place this component at the top of your page layout alongside <AppSidebar />.
 */
export function MobileTopBar() {
  const { toggleSidebar, openMobile } = useSidebar();

  return (
    <header className="md:hidden sticky top-0 z-50 flex items-center justify-between px-4 h-14 bg-card border-b border-border/60 shadow-sm">
      {/* Brand */}
      <div className="flex items-center gap-2.5">
        <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-primary to-primary/60 flex items-center justify-center shadow shadow-primary/20">
          <Home className="w-4 h-4 text-white" />
        </div>
        <span className="text-sm font-extrabold text-foreground tracking-tight">RoomShare AI</span>
      </div>

      {/* Hamburger */}
      <button
        onClick={toggleSidebar}
        aria-label="Toggle navigation"
        className="w-9 h-9 flex items-center justify-center rounded-lg text-muted-foreground hover:bg-muted hover:text-foreground transition-colors"
      >
        {openMobile ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
      </button>
    </header>
  );
}

// ─── Nav Item ─────────────────────────────────────────────────────────────────

function NavItem({ item, onNavigate }) {
  const { state } = useSidebar();
  const isIconMode = state === "collapsed";

  return (
    <SidebarMenuItem>
      <SidebarMenuButton asChild tooltip={isIconMode ? item.label : undefined}>
        <NavLink
          to={item.to}
          end={item.end}
          onClick={onNavigate}
          className={({ isActive }) =>
            cn(
              "flex items-center gap-3 rounded-xl transition-all duration-150 select-none",
              // Padding: tighter in icon mode, normal otherwise
              isIconMode ? "justify-center p-2" : "px-3 py-2",
              isActive
                ? "bg-primary/10 text-primary font-bold"
                : "text-muted-foreground hover:bg-muted hover:text-foreground active:scale-[0.98]"
            )
          }
        >
          <item.icon
            className={cn(
              "shrink-0",
              isIconMode ? "w-5 h-5" : "w-[18px] h-[18px]",
              item.premium && "text-amber-500"
            )}
          />

          {/* Label + badge — hidden in icon mode */}
          {!isIconMode && (
            <>
              <span className="text-xs leading-none">{item.label}</span>
              {item.premium && (
                <span className="ml-auto text-[9px] bg-amber-100 text-amber-600 px-1.5 py-0.5 rounded-full font-bold uppercase tracking-wider">
                  AI
                </span>
              )}
            </>
          )}
        </NavLink>
      </SidebarMenuButton>
    </SidebarMenuItem>
  );
}

// ─── Section ──────────────────────────────────────────────────────────────────

function NavSection({ label, items, onNavigate }) {
  const { state } = useSidebar();
  const isIconMode = state === "collapsed";

  return (
    <SidebarGroup>
      {!isIconMode && (
        <SidebarGroupLabel className="px-4 text-[10px] font-bold uppercase tracking-[0.2em] text-muted-foreground/50 mb-1">
          {label}
        </SidebarGroupLabel>
      )}
      <SidebarGroupContent>
        <SidebarMenu className={cn("space-y-0.5", isIconMode ? "px-1" : "px-2")}>
          {items.map(item => (
            <NavItem key={item.to} item={item} onNavigate={onNavigate} />
          ))}
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  );
}

// ─── Main Sidebar ─────────────────────────────────────────────────────────────

export function AppSidebar() {
  const { user, logout, isPremium } = useAuth();
  const navigate = useNavigate();
  const { setOpenMobile, isMobile, state } = useSidebar();
  const isIconMode = state === "collapsed";

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  // Close mobile sheet on nav
  const handleNavigate = () => {
    if (isMobile) setOpenMobile(false);
  };

  const displayName = user?.fullName || user?.email?.split('@')[0] || 'User';
  const initials = displayName[0].toUpperCase();

  return (
    <Sidebar
      collapsible="icon"
      className="border-r border-border/60 bg-card"
    >
      {/* ── Header ─────────────────────────────────────────────────────────── */}
      <SidebarHeader className="h-16 flex flex-row items-center px-3 border-b border-border/50 gap-2">
        <div className="flex items-center gap-3 flex-1 overflow-hidden">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-primary to-primary/60 flex items-center justify-center shrink-0 shadow-md shadow-primary/20">
            <Home className="w-4 h-4 text-white" />
          </div>
          <div className={cn("transition-opacity duration-200", isIconMode ? "opacity-0 w-0 overflow-hidden" : "opacity-100")}>
            <h1 className="text-sm font-extrabold text-foreground leading-tight tracking-tight whitespace-nowrap">
              RoomShare AI
            </h1>
            <p className="text-[10px] text-muted-foreground font-medium -mt-0.5">Premium Housing</p>
          </div>
        </div>

        {/* Desktop collapse toggle */}
        <SidebarTrigger className="hidden md:flex shrink-0 text-muted-foreground hover:text-foreground" />
      </SidebarHeader>

      {/* ── Content ────────────────────────────────────────────────────────── */}
      <SidebarContent className="py-3 overflow-y-auto overflow-x-hidden">
        <NavSection label="Main Menu" items={mainNav} onNavigate={handleNavigate} />

        <SidebarSeparator className="my-2 opacity-40" />

        <NavSection label="Personal" items={personalNav} onNavigate={handleNavigate} />

        <SidebarSeparator className="my-2 opacity-40" />

        <NavSection label="Other" items={otherNav} onNavigate={handleNavigate} />
      </SidebarContent>

      {/* ── Footer ─────────────────────────────────────────────────────────── */}
      <SidebarFooter className={cn("border-t border-border/50", isIconMode ? "p-2" : "p-3")}>
        {/* User info */}
        <div className={cn("flex items-center gap-3", isIconMode && "justify-center")}>
          <div
            className="w-9 h-9 rounded-full bg-gradient-to-br from-primary to-primary/60 flex items-center justify-center text-white font-bold text-sm shadow border-2 border-background shrink-0"
            title={displayName}
          >
            {initials}
          </div>

          {!isIconMode && (
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-1.5">
                <p className="text-xs font-bold text-foreground truncate">{displayName}</p>
                {isPremium && (
                  <div className="w-3.5 h-3.5 rounded-full bg-amber-500 flex items-center justify-center shadow-sm shadow-amber-500/20">
                    <Star className="w-2 h-2 text-white fill-white" />
                  </div>
                )}
              </div>
              <p className="text-[10px] text-muted-foreground truncate font-medium">
                {isPremium ? "Premium Member" : user?.email}
              </p>
            </div>
          )}
        </div>

        {/* Sign out */}
        <button
          onClick={handleLogout}
          aria-label="Sign out"
          className={cn(
            "flex items-center gap-3 rounded-xl text-destructive hover:bg-destructive/10 transition-all text-xs font-bold mt-1",
            isIconMode ? "justify-center p-2" : "px-3 py-2"
          )}
        >
          <LogOut className="w-4 h-4 shrink-0" />
          {!isIconMode && <span>Sign Out</span>}
        </button>
      </SidebarFooter>
    </Sidebar>
  );
}