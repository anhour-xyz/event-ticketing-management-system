import { Button } from "@/components/ui/button";
import { countPurchasedTickets } from "@/lib/api";
import { ShoppingCart } from "lucide-react";
import { useEffect, useState } from "react";
import { useAuth } from "react-oidc-context";
import { useNavigate } from "react-router";

const TicketCartButton = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [count, setCount] = useState(0);

  useEffect(() => {
    if (!user?.access_token) {
      setCount(0);
      return;
    }

    countPurchasedTickets(user.access_token)
      .then(setCount)
      .catch(() => setCount(0));
  }, [user?.access_token]);

  return (
    <Button
      type="button"
      className="relative"
      onClick={() => navigate("/dashboard/tickets")}
      aria-label={`My tickets, ${count} purchased`}
    >
      <ShoppingCart />

      {count > 0 && (
        <span className="absolute -right-2 -top-2 min-w-5 rounded-full bg-red-500 px-1 text-xs">
          {count}
        </span>
      )}
    </Button>
  );
};

export default TicketCartButton;
