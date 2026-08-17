import NavBar from "@/components/nav-bar";
import { SimplePagination } from "@/components/simple-pagination";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import {
  SpringBootPagination,
  TicketStatus,
  TicketSummary,
} from "@/domain/domain";
import { cancelTicket, listTickets } from "@/lib/api";
import { AlertCircle, DollarSign, Tag, Ticket } from "lucide-react";
import { useEffect, useState } from "react";
import { useAuth } from "react-oidc-context";
import { Link } from "react-router";
import { Button } from "@/components/ui/button";

const DashboardListTickets: React.FC = () => {
  const { isLoading, user } = useAuth();

  const [tickets, setTickets] = useState<
    SpringBootPagination<TicketSummary> | undefined
  >();
  const [error, setError] = useState<string | undefined>();
  const [page, setPage] = useState(0);

  const refreshTickets = async () => {
    if (!user?.access_token) return;
    setTickets(await listTickets(user.access_token, page));
  };

  const handleCancel = async (ticketId: string) => {
    if (!user?.access_token) return;

    try {
      setError(undefined);
      await cancelTicket(user.access_token, ticketId);
      await refreshTickets();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to cancel ticket");
    }
  };

  useEffect(() => {
    if (isLoading || !user?.access_token) {
      return;
    }

    const doUseEffect = async () => {
      try {
        setTickets(await listTickets(user.access_token, page));
      } catch (err) {
        if (err instanceof Error) {
          setError(err.message);
        } else if (typeof err === "string") {
          setError(err);
        } else {
          setError("An unknown error occurred");
        }
      }
    };

    doUseEffect();
  }, [isLoading, user?.access_token, page]);

  if (error) {
    return (
      <div className="bg-black min-h-screen text-white">
        <NavBar />
        <Alert variant="destructive" className="bg-gray-900 border-red-700">
          <AlertCircle className="h-4 w-4" />
          <AlertTitle>Error</AlertTitle>
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      </div>
    );
  }

  return (
    <div className="bg-black min-h-screen text-white">
      <NavBar />

      {/* Title */}
      <div className="py-8 px-4">
        <h1 className="text-2xl font-bold">Your Tickets</h1>
        <p>Tickets you have purchased</p>
      </div>

      <div className="max-w-lg mx-auto">
        {tickets?.content.map((ticketItem) => (
          <Card key={ticketItem.id} className="mb-4 bg-gray-900 text-white">
            <Link to={`/dashboard/tickets/${ticketItem.id}`}>
              <CardHeader>
                <div className="flex justify-between">
                  <div className="flex items-center gap-2">
                    <Ticket className="h-5 w-5 text-gray-400" />
                    <h3 className="font-bold text-xl">
                      {ticketItem.ticketType.name}
                    </h3>
                  </div>
                  <span>{ticketItem.status}</span>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {/* Price */}
                <div className="flex items-center gap-2">
                  <DollarSign className="h-5 w-5 text-gray-400" />
                  <p className="font-medium">${ticketItem.ticketType.price}</p>
                </div>

                {/* Ticket ID */}
                <div className="flex items-center gap-2">
                  <Tag className="h-5 w-5 text-gray-400" />
                  <div>
                    <h4 className="font-medium">Ticket ID</h4>
                    <p className="text-gray-400 font-mono text-sm">
                      {ticketItem.id}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Link>
            {ticketItem.status === TicketStatus.PURCHASED && (
              <CardContent>
                <Button
                  type="button"
                  variant="destructive"
                  onClick={() => handleCancel(ticketItem.id)}
                >
                  Cancel ticket
                </Button>
              </CardContent>
            )}
          </Card>
        ))}
      </div>
      <div className="flex justify-center py-8">
        {tickets && (
          <SimplePagination pagination={tickets} onPageChange={setPage} />
        )}
      </div>
    </div>
  );
};

export default DashboardListTickets;
