import { ReactNode } from "react";
import { Navigate } from "react-router";
import { useRoles } from "@/hooks/use-roles";

interface OrganizerRouteProperties {
  children: ReactNode;
}

const OrganizerRoute = ({ children }: OrganizerRouteProperties) => {
  const { isLoading, isOrganizer } = useRoles();

  if (isLoading) return <p>Loading...</p>;
  if (!isOrganizer) return <Navigate to="/dashboard/tickets" replace />;

  return children;
};

export default OrganizerRoute;
