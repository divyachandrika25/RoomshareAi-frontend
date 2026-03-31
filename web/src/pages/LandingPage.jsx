import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Home, Users, Shield, Sparkles, Star, 
  ArrowRight, Search, Zap, Globe, 
  Play, Menu, X, ArrowUpRight, CheckCircle2, 
} from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

export default function LandingPage() {
  const navigate = useNavigate();
  const [mobileMenu, setMobileMenu] = useState(false);
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const features = [
    { icon: Sparkles, title: 'Neural Matching', desc: 'Our transformers analyze 200+ behavioral tags to predict compatibility with 98% accuracy.', color: 'from-indigo-600 to-violet-700' },
    { icon: Shield, title: 'Biometric Trust', desc: 'Secure identity linking ensures every roommate profile is backed by verified governmental data.', color: 'from-emerald-600 to-teal-700' },
    { icon: Zap, title: 'Hotel AI Agent', desc: 'Real-time booking engine scans 500+ properties per second to find deals within your budget.', color: 'from-amber-500 to-orange-600' },
    { icon: Globe, title: 'Smart Contracts', desc: 'Digital agreements with automated escrow protect your security deposits instantly.', color: 'from-purple-600 to-pink-700' },
  ];

  return (
    <div className="min-h-screen bg-background selection:bg-indigo-500/30 selection:text-indigo-200 overflow-x-hidden text-foreground">
      {/* Dynamic Glass Navbar */}
      <header className={cn(
        "fixed top-0 left-0 right-0 z-[100] transition-all duration-500 px-4 py-4 md:px-8",
        scrolled ? "md:py-4" : "md:py-8"
      )}>
        <nav className={cn(
          "max-w-7xl mx-auto px-6 h-20 rounded-2xl flex items-center justify-between transition-all duration-500 border border-transparent",
          scrolled && "bg-background/80 backdrop-blur-2xl border-border shadow-2xl shadow-black/40"
        )}>
          <div className="flex items-center gap-3 group cursor-pointer" onClick={() => navigate('/')}>
           
            <div className="flex flex-col">
              <span className="text-xl font-black text-foreground leading-none tracking-tight">RoomShare</span>
              <span className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.3em] mt-1">Intelligence</span>
            </div>
          </div>
          
          <nav className="hidden lg:flex items-center gap-12">
            {['Intelligence', 'Protocols', 'Enterprise', 'Security'].map(item => (
              <a key={item} href={`#${item.toLowerCase()}`} className="text-[11px] font-black text-muted-foreground hover:text-indigo-400 uppercase tracking-[0.3em] transition-colors">{item}</a>
            ))}
          </nav>
          
          <div className="hidden md:flex items-center gap-4">
            <Button variant="ghost" onClick={() => navigate('/login')} className="px-6 rounded-xl font-black text-sm text-foreground hover:bg-white/5">Log In</Button>
            <Button onClick={() => navigate('/register')} className="px-8 h-12 rounded-xl font-black text-sm bg-indigo-600 text-white hover:bg-indigo-700 shadow-2xl shadow-indigo-600/20">Register Access</Button>
          </div>

          <Button variant="ghost" size="icon" className="md:hidden rounded-xl h-12 w-12 text-foreground" onClick={() => setMobileMenu(!mobileMenu)}>
            {mobileMenu ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
          </Button>
        </nav>

        {/* Mobile Navigation */}
        <div className={cn(
           "md:hidden absolute top-28 left-4 right-4 bg-card/95 backdrop-blur-2xl rounded-2xl border border-border shadow-2xl p-8 space-y-6 transition-all duration-500 origin-top",
           mobileMenu ? "scale-y-100 opacity-100" : "scale-y-0 opacity-0 pointer-events-none"
        )}>
          {['Product', 'AI Matching', 'Pricing', 'Docs'].map(item => (
            <a key={item} href="#" className="block py-2 text-lg font-black text-foreground tracking-tight">{item}</a>
          ))}
          <div className="pt-6 border-t border-border grid gap-4">
             <Button variant="outline" onClick={() => navigate('/login')} className="h-14 rounded-xl font-black text-lg border-2">Sign In</Button>
             <Button onClick={() => navigate('/register')} className="h-14 rounded-xl font-black text-lg bg-indigo-600 text-white">Get Started</Button>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="relative pt-40 md:pt-60 pb-20 md:pb-40 px-6">
        <div className="absolute top-0 right-0 w-[800px] h-[800px] bg-indigo-600/10 rounded-full blur-[150px] -mr-96 -mt-96 opacity-60" />
        <div className="absolute top-1/2 left-0 w-[600px] h-[600px] bg-violet-600/5 rounded-full blur-[120px] -ml-64 opacity-40" />
        
        <div className="max-w-7xl mx-auto flex flex-col lg:flex-row items-center gap-24 relative z-10">
          <div className="flex-1 text-center lg:text-left space-y-10">
            <div className="inline-flex items-center gap-3 px-6 py-2.5 rounded-full bg-indigo-600/10 border border-indigo-500/20 shadow-xl shadow-indigo-600/5">
              <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
              <span className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.3em]">v3.0 Neural Engine Live</span>
            </div>
            
            <h1 className="text-5xl sm:text-7xl lg:text-8xl font-black text-foreground leading-[0.9] tracking-tighter">
              The Architecture <br />
              <span className="text-indigo-500 italic">of Living.</span>
            </h1>
            
            <p className="text-xl md:text-2xl text-muted-foreground font-medium leading-relaxed max-w-2xl mx-auto lg:mx-0 opacity-80">
               Automated roommate matching and premium hospitality discovery. 
               Experience the future of co-living with AI-verified synergies.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-6 justify-center lg:justify-start pt-6">
              <Button onClick={() => navigate('/register')} size="lg" className="h-20 px-12 rounded-2xl bg-indigo-600 text-white font-black text-xl hover:bg-indigo-700 shadow-2xl shadow-indigo-600/30 group transition-all">
                Get Started <ArrowRight className="w-6 h-6 ml-3 group-hover:translate-x-2 transition-transform" />
              </Button>
             
            </div>
            
            <div className="flex flex-wrap items-center justify-center lg:justify-start gap-12 pt-12">
               {[
                 { v: '18M+', l: 'Signals Analyzed' },
                 { v: '98.4%', l: 'Accuracy' },
                 { v: '250', l: 'Premium Assets' }
               ].map((s, i) => (
                 <div key={i} className="space-y-1">
                    <p className="text-4xl font-black text-foreground tracking-tighter">{s.v}</p>
                    <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">{s.l}</p>
                 </div>
               ))}
            </div>
          </div>

          <div className="flex-1 w-full max-w-2xl relative order-first lg:order-last">
             <div className="relative group">
                <div className="absolute inset-0 bg-indigo-600/20 blur-[100px] rounded-full scale-125 opacity-20 -z-10" />
                
                {/* Visual Interface Mockup */}
                <div className="bg-card/80 backdrop-blur-3xl rounded-3xl p-10 border border-border shadow-2xl relative transform lg:rotate-3 hover:rotate-0 transition-transform duration-1000 overflow-hidden">
                   <div className="flex items-center justify-between mb-12">
                      <div className="flex gap-2">
                        {[1,2,3].map(i => <div key={i} className="w-3 h-3 rounded-full bg-muted/30" />)}
                      </div>
                      <Badge className="bg-emerald-500/10 text-emerald-400 border-none font-black text-[9px] tracking-widest uppercase py-1 px-3">Identity Verified</Badge>
                   </div>
                   
                   <div className="flex items-center gap-8 mb-12">
                      <div className="relative">
                        <div className="w-32 h-32 rounded-2xl bg-gradient-to-br from-indigo-500 to-violet-600 p-1">
                           <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format" alt="" className="w-full h-full object-cover rounded-2xl border-4 border-card" />
                        </div>
                        <div className="absolute -bottom-2 -right-2 w-10 h-10 bg-indigo-600 rounded-xl border-4 border-card flex items-center justify-center shadow-lg">
                           <Sparkles className="w-5 h-5 text-white" />
                        </div>
                      </div>
                      <div>
                        <h3 className="text-3xl font-black text-foreground">Alexander Ray</h3>
                        <p className="text-indigo-400 font-black uppercase text-[10px] tracking-widest mt-2">Executive Resident • Chennai</p>
                      </div>
                   </div>
                   
                   <div className="space-y-6">
                      <div className="flex items-center justify-between">
                         <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Compatibility Alpha</span>
                         <span className="text-2xl font-black text-foreground">97.4%</span>
                      </div>
                      <div className="h-4 w-full bg-muted rounded-full overflow-hidden">
                         <div className="h-full bg-indigo-500 rounded-full animate-grow-width shadow-lg shadow-indigo-500/20" style={{ width: '97%' }} />
                      </div>
                   </div>
                   
                   <div className="mt-12 p-6 bg-indigo-600 rounded-2xl text-white shadow-xl">
                      <p className="text-xs font-black uppercase tracking-[0.3em] opacity-60 mb-4">Neural Analysis Summary</p>
                      <p className="text-sm font-medium leading-relaxed italic">"Dynamic synergy detected in work-from-home cycles. Combined cleanliness standard: Purity Grade A."</p>
                   </div>
                </div>

                {/* Floating Elements */}
                <div className="absolute -top-10 -left-10 bg-card p-6 rounded-2xl shadow-2xl border border-border animate-float hidden md:block">
                   <div className="flex items-center gap-4">
                      <div className="w-12 h-12 rounded-xl bg-amber-500/10 flex items-center justify-center">
                         <Star className="w-6 h-6 text-amber-500 fill-amber-500" />
                      </div>
                      <div>
                         <p className="text-xl font-black text-foreground">4.98/5</p>
                         <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Global Rating</p>
                      </div>
                   </div>
                </div>
                
                <div className="absolute -bottom-10 -right-10 bg-indigo-600 text-white p-8 rounded-2xl shadow-2xl animate-float-delayed hidden md:block">
                   <Users className="w-10 h-10 mb-4 text-indigo-200" />
                   <p className="text-2xl font-black leading-tight">12,482</p>
                   <p className="text-[10px] font-black opacity-60 uppercase tracking-widest">Success Matches</p>
                </div>
             </div>
          </div>
        </div>
      </section>

      {/* Feature Grid */}
      <section className="py-20 px-6 bg-secondary/30">
        <div className="max-w-7xl mx-auto space-y-24">
          <div className="text-center space-y-4">
             <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-indigo-500/10 text-indigo-400 border border-indigo-500/20">
                <span className="text-[10px] font-black uppercase tracking-widest">Advanced Integrity Protocols</span>
             </div>
             <h2 className="text-4xl md:text-6xl font-black text-foreground tracking-tighter">Engineered for Humans.</h2>
             <p className="text-lg text-muted-foreground max-w-2xl mx-auto font-medium">We replaced guessing with science. Experience the only co-living platform that understands personal dynamics.</p>
          </div>
          
          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
            {features.map((f, i) => (
              <div key={i} className="group relative p-10 rounded-2xl bg-card border border-border hover:border-indigo-500/40 hover:shadow-2xl hover:shadow-indigo-500/10 transition-all duration-500 overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-indigo-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity" />
                <div className={cn(
                  "w-20 h-20 rounded-2xl flex items-center justify-center mb-8 bg-gradient-to-br shadow-xl group-hover:scale-110 transition-transform relative z-10",
                  f.color
                )}>
                  <f.icon className="w-10 h-10 text-white" />
                </div>
                <h3 className="text-2xl font-black text-foreground mb-4 relative z-10">{f.title}</h3>
                <p className="text-base text-muted-foreground font-medium leading-relaxed relative z-10">{f.desc}</p>
                <div className="mt-8 relative z-10">
                   <Button variant="link" className="p-0 h-auto font-black text-xs uppercase tracking-widest text-indigo-400 hover:text-indigo-300 transition-colors">Learn Mechanics <ArrowUpRight className="w-4 h-4 ml-2" /></Button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Social Proof */}
      <section className="py-20 bg-indigo-900 border-y border-indigo-800 text-white relative overflow-hidden">
         <div className="absolute top-0 left-0 w-full h-full bg-[radial-gradient(circle_at_center,_rgba(99,102,241,0.1)_0%,_transparent_70%)]" />
         <div className="max-w-7xl mx-auto px-6 relative z-10">
            <div className="flex flex-col md:flex-row items-center justify-between gap-12">
               <div className="max-w-xl text-center md:text-left">
                  <h3 className="text-4xl md:text-5xl font-black mb-6 leading-tight">Trusted by global residents across 50+ hyper-cities.</h3>
                  <p className="text-indigo-200/60 text-lg font-medium">Join the residency of the future today. Our decentralized matching ensures local relevance with international standards.</p>
               </div>
               <div className="grid grid-cols-2 gap-x-12 gap-y-12 shrink-0">
                  <div className="text-center">
                    <p className="text-5xl font-black mb-2 tracking-tighter text-indigo-400">98%</p>
                    <p className="text-[10px] font-black uppercase tracking-widest text-indigo-200/40">Success Rate</p>
                  </div>
                  <div className="text-center">
                    <p className="text-5xl font-black mb-2 tracking-tighter text-white">2h</p>
                    <p className="text-[10px] font-black uppercase tracking-widest text-indigo-200/40">Avg. Match Time</p>
                  </div>
                  <div className="text-center">
                    <p className="text-5xl font-black mb-2 tracking-tighter text-white">500+</p>
                    <p className="text-[10px] font-black uppercase tracking-widest text-indigo-200/40">Premium PGs</p>
                  </div>
                  <div className="text-center">
                    <p className="text-5xl font-black mb-2 tracking-tighter text-emerald-400">Secure</p>
                    <p className="text-[10px] font-black uppercase tracking-widest text-indigo-200/40">Bank-Grade Tech</p>
                  </div>
               </div>
            </div>
         </div>
      </section>

      {/* CTA Final */}
      <section className="py-40 px-6">
         <div className="max-w-6xl mx-auto relative group">
            <div className="absolute inset-0 bg-indigo-600/20 blur-[150px] rounded-full scale-125 opacity-20 group-hover:opacity-40 transition-opacity" />
            <div className="bg-gradient-to-br from-indigo-700 to-violet-800 rounded-3xl p-16 md:p-24 text-center text-white relative z-10 shadow-2xl overflow-hidden border border-indigo-500/20">
               <div className="absolute top-0 right-0 w-96 h-96 bg-white/5 rounded-full blur-3xl -mr-48 -mt-48" />
               <div className="absolute bottom-0 left-0 w-64 h-64 bg-black/20 rounded-full blur-2xl -ml-32 -mb-32" />
               
               <h2 className="text-5xl md:text-7xl font-black mb-8 leading-[0.9] tracking-tighter">Your Next Evolution <br /> <span className="text-indigo-300">Starts Here.</span></h2>
               <p className="text-xl md:text-2xl text-indigo-100/60 max-w-2xl mx-auto mb-12 font-medium">Join 50,000+ residents who have optimized their living situation through neural intelligence.</p>
               
               <div className="flex flex-col sm:flex-row gap-6 justify-center">
                  <Button onClick={() => navigate('/register')} size="lg" className="h-20 px-12 rounded-2xl bg-white text-indigo-900 font-black text-xl hover:bg-white/90 transition-all shadow-2xl">
                    Create Free Profile
                  </Button>
                  <Button size="lg" variant="ghost" className="h-20 px-12 rounded-2xl font-black text-xl text-white hover:bg-white/10">
                    Contact Enterprise
                  </Button>
               </div>
               
               <div className="mt-12 flex items-center justify-center gap-8 opacity-40">
                  <div className="flex items-center gap-2">
                     <CheckCircle2 className="w-5 h-5" />
                     <span className="text-[10px] font-black uppercase tracking-widest">No Card Required</span>
                  </div>
                  <div className="flex items-center gap-2">
                     <CheckCircle2 className="w-5 h-5" />
                     <span className="text-[10px] font-black uppercase tracking-widest">Instant Activation</span>
                  </div>
               </div>
            </div>
         </div>
      </section>

      {/* Footer */}
      <footer className="py-20 px-8 border-t border-border">
         <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-12">
            <div>
               <div className="flex items-center gap-3">
                  <div className="w-10 h-10 bg-indigo-600 rounded-xl flex items-center justify-center shadow-lg shadow-indigo-600/20 p-2">
                     <img src="/app_logo.png" alt="RoomShare" className="w-full h-full object-contain brightness-0 invert" />
                  </div>
                  <span className="text-2xl font-black text-foreground tracking-tighter">RoomShare</span>
               </div>
               <p className="text-sm text-muted-foreground font-medium mt-4">Redefining co-living through neural synergy.</p>
            </div>
            
            <div className="flex gap-12 text-[10px] font-black uppercase tracking-[0.3em] text-muted-foreground">
               <a href="#" className="hover:text-indigo-400 transition-colors">Privacy Paradigm</a>
               <a href="#" className="hover:text-indigo-400 transition-colors">Legal Framework</a>
               <a href="#" className="hover:text-indigo-400 transition-colors">Residency Protocol</a>
            </div>
            
            <div className="flex gap-6">
               {['Twitter', 'Instagram', 'Github'].map(s => (
                 <a key={s} href="#" className="w-12 h-12 rounded-xl bg-muted/30 flex items-center justify-center hover:bg-indigo-500/10 hover:text-indigo-400 transition-all font-black text-xs text-muted-foreground">{s[0]}</a>
               ))}
            </div>
         </div>
         <p className="text-center text-[10px] font-black text-muted-foreground/30 uppercase tracking-[0.4em] mt-20">© 2026 RoomShare Intelligent Systems.</p>
      </footer>
    </div>
  );
}
