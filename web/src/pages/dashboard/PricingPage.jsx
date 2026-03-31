import { useAuth } from '@/contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { Check, Sparkles, Star, Zap } from 'lucide-react';
import { Button } from "@/components/ui/button";

export default function PricingPage() {
  const { user, setPremium, isPremium } = useAuth();
  const navigate = useNavigate();

  const handleSubscribe = () => {
    // Razorpay Integration (Live/Test)
    const options = {
      key: import.meta.env.VITE_RAZORPAY_KEY_ID || "rzp_test_SWKknvSDD1VSWN", // Get from .env
      amount: 10000, // Amount in paise (100 INR)
      currency: "INR",
      name: "RoomShare AI",
      description: "Premium Membership",
      handler: function (response) {
        // This is called after successful payment
        setPremium(true);
        alert('Payment Successful! Welcome to RoomShare Premium.');
        navigate('/dashboard/home');
      },
      prefill: {
        name: user?.fullName || "",
        email: user?.email || "",
      },
      theme: { color: "#1E63FF" },
    };

    try {
      if (!window.Razorpay) {
        alert("Payment gateway failed to load. Please check your internet connection.");
        return;
      }
      const rzp1 = new window.Razorpay(options);
      rzp1.open();
    } catch (err) {
      console.error("Razorpay Error:", err);
    }
  };

  const plans = [
    {
      name: 'Free',
      price: '₹0',
      features: ['Basic Room Search', '5 AI Assistant queries/day', 'Standard Matches'],
      button: 'Current Plan',
      disabled: true,
      icon: Zap
    },
    {
      name: 'Premium',
      price: isPremium ? '₹100' : '₹149',
      period: isPremium ? ' (Active)' : '/month',
      features: [
        'Unlimited AI Assistant',
        'Advanced Matching Engine',
        'Verified Badge on Profile',
        'Contact Roommates Directly',
        'Priority Listing',
        'Early access to new rooms'
      ],
      button: isPremium ? 'Active Plan' : 'Upgrade Now',
      disabled: isPremium,
      popular: true,
      icon: Star
    }
  ];

  return (
    <div className="max-w-5xl mx-auto py-12 px-6">
      <div className="text-center mb-16">
        <h1 className="text-4xl font-black text-foreground mb-4">Choose Your Plan</h1>
        <p className="text-muted-foreground max-w-xl mx-auto">
          Unlock the full power of RoomShare AI and find your perfect room or roommate 10x faster.
        </p>
      </div>

      <div className="grid md:grid-cols-2 gap-8 max-w-4xl mx-auto">
        {plans.map((plan, i) => (
          <div 
            key={i} 
            className={`relative rounded-3xl border ${plan.popular ? 'border-primary shadow-2xl shadow-primary/10 bg-card' : 'border-border bg-card/50'} p-8 flex flex-col`}
          >
            {plan.popular && (
              <div className="absolute -top-4 left-1/2 -translate-x-1/2 bg-primary text-primary-foreground text-[10px] font-black uppercase tracking-widest px-4 py-1.5 rounded-full flex items-center gap-1.5 shadow-lg shadow-primary/20">
                <Sparkles className="w-3 h-3" /> Most Popular
              </div>
            )}

            <div className="mb-8">
              <div className="w-12 h-12 rounded-2xl bg-primary/10 flex items-center justify-center mb-4">
                <plan.icon className="w-6 h-6 text-primary" />
              </div>
              <h2 className="text-2xl font-black text-foreground mb-2">{plan.name}</h2>
              <div className="flex items-baseline gap-1">
                <span className="text-4xl font-black text-foreground">{plan.price}</span>
                {plan.period && <span className="text-muted-foreground font-bold">{plan.period}</span>}
              </div>
            </div>

            <div className="space-y-4 mb-10 flex-1">
              {plan.features.map((feat, j) => (
                <div key={j} className="flex items-start gap-3">
                  <div className="w-5 h-5 rounded-full bg-emerald-500/10 flex items-center justify-center shrink-0 mt-0.5">
                    <Check className="w-3 h-3 text-emerald-500" />
                  </div>
                  <span className="text-sm font-medium text-foreground/80 leading-relaxed">{feat}</span>
                </div>
              ))}
            </div>

            <Button 
              onClick={plan.disabled ? null : handleSubscribe}
              disabled={plan.disabled}
              className={`w-full h-12 rounded-xl font-black text-sm uppercase tracking-wider transition-all shadow-md ${plan.popular ? 'shadow-primary/20 hover:-translate-y-1' : ''}`}
              variant={plan.popular ? 'default' : 'outline'}
            >
              {plan.button}
            </Button>
          </div>
        ))}
      </div>

      <div className="mt-12 text-center">
         <button 
           onClick={() => navigate(-1)}
           className="text-xs font-bold text-muted-foreground hover:text-primary transition-colors underline decoration-primary/30 underline-offset-4"
         >
           Take me back, maybe later
         </button>
      </div>
    </div>
  );
}
