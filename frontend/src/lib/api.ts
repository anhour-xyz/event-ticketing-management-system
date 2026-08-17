import {
  CreateEventRequest,
  EventDetails,
  EventSummary,
  isErrorResponse,
  PublishedEventDetails,
  PublishedEventSummary,
  SpringBootPagination,
  TicketDetails,
  TicketSummary,
  TicketValidationRequest,
  TicketValidationResponse,
  UpdateEventRequest,
} from "@/domain/domain";
import mockDatabase from "../../db.json";

const API_URL = (import.meta.env.VITE_API_URL ?? "").replace(/\/$/, "");
const USE_PRODUCTION_MOCK = import.meta.env.PROD && !API_URL;

const mockPublishedEvents = mockDatabase[
  "published-events"
] as unknown as PublishedEventDetails[];

const apiFetch = (path: string, init?: RequestInit) =>
  fetch(`${API_URL}${path}`, init);

const parseJsonResponse = async (response: Response) => {
  const body = await response.text();

  if (!body.trim()) {
    if (!response.ok) {
      throw new Error(`API request failed (${response.status})`);
    }
    return undefined;
  }

  try {
    return JSON.parse(body);
  } catch {
    throw new Error(
      response.ok
        ? "API returned an invalid JSON response"
        : `API request failed (${response.status})`,
    );
  }
};

const normalizePagination = <T>(
  body: SpringBootPagination<T> | T[],
  page: number,
  size: number,
): SpringBootPagination<T> => {
  if (!Array.isArray(body)) return body;

  const content = body.slice(page * size, (page + 1) * size);
  const totalPages = Math.ceil(body.length / size);
  const sort = { empty: true, sorted: false, unsorted: true };

  return {
    content,
    pageable: {
      sort,
      offset: page * size,
      pageNumber: page,
      pageSize: size,
      paged: true,
      unpaged: false,
    },
    last: totalPages === 0 || page >= totalPages - 1,
    totalElements: body.length,
    totalPages,
    size,
    number: page,
    sort,
    first: page === 0,
    numberOfElements: content.length,
    empty: content.length === 0,
  };
};

export const createEvent = async (
  accessToken: string,
  request: CreateEventRequest,
): Promise<void> => {
  const response = await apiFetch("/api/v1/events", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }
};

export const updateEvent = async (
  accessToken: string,
  id: string,
  request: UpdateEventRequest,
): Promise<void> => {
  const response = await apiFetch(`/api/v1/events/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }
};

export const listEvents = async (
  accessToken: string,
  page: number,
): Promise<SpringBootPagination<EventSummary>> => {
  const response = await apiFetch(`/api/v1/events?page=${page}&size=2`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return normalizePagination(responseBody, page, 2);
};

export const getEvent = async (
  accessToken: string,
  id: string,
): Promise<EventDetails> => {
  const response = await apiFetch(`/api/v1/events/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return responseBody as EventDetails;
};

export const deleteEvent = async (
  accessToken: string,
  id: string,
): Promise<void> => {
  const response = await apiFetch(`/api/v1/events/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    const responseBody = await parseJsonResponse(response);
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }
};

export const listPublishedEvents = async (
  page: number,
): Promise<SpringBootPagination<PublishedEventSummary>> => {
  if (USE_PRODUCTION_MOCK) {
    return normalizePagination(mockPublishedEvents, page, 4);
  }

  const response = await apiFetch(
    `/api/v1/published-events?page=${page}&size=4`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    },
  );

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return normalizePagination(responseBody, page, 4);
};

export const searchPublishedEvents = async (
  query: string,
  page: number,
): Promise<SpringBootPagination<PublishedEventSummary>> => {
  if (USE_PRODUCTION_MOCK) {
    const normalizedQuery = query.trim().toLocaleLowerCase();
    const matches = mockPublishedEvents.filter(
      (event) =>
        event.name.toLocaleLowerCase().includes(normalizedQuery) ||
        event.venue.toLocaleLowerCase().includes(normalizedQuery),
    );

    return normalizePagination(matches, page, 4);
  }

  const response = await apiFetch(
    `/api/v1/published-events?q=${query}&page=${page}&size=4`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    },
  );

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return normalizePagination(responseBody, page, 4);
};

export const getPublishedEvent = async (
  id: string,
): Promise<PublishedEventDetails> => {
  if (USE_PRODUCTION_MOCK) {
    const event = mockPublishedEvents.find((candidate) => candidate.id === id);

    if (!event) {
      throw new Error("Event not found");
    }

    return event;
  }

  const response = await apiFetch(`/api/v1/published-events/${id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return responseBody as PublishedEventDetails;
};

export const purchaseTicket = async (
  accessToken: string,
  eventId: string,
  ticketTypeId: string,
): Promise<void> => {
  const response = await apiFetch(
    `/api/v1/events/${eventId}/ticket-types/${ticketTypeId}/tickets`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
    },
  );

  if (!response.ok) {
    const responseBody = await parseJsonResponse(response);
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }
};

export const listTickets = async (
  accessToken: string,
  page: number,
): Promise<SpringBootPagination<TicketSummary>> => {
  const response = await apiFetch(`/api/v1/tickets?page=${page}&size=8`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return normalizePagination(responseBody, page, 8);
};

export const countPurchasedTickets = async (
  accessToken: string,
): Promise<number> => {
  const response = await apiFetch("/api/v1/tickets/count", {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    throw new Error(
      isErrorResponse(responseBody)
        ? responseBody.error
        : `Unable to count tickets (${response.status})`,
    );
  }

  return (responseBody as { count: number }).count;
};

export const cancelTicket = async (
  accessToken: string,
  ticketId: string,
): Promise<void> => {
  const response = await apiFetch(`/api/v1/tickets/${ticketId}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  if (!response.ok) {
    const responseBody = await parseJsonResponse(response);
    throw new Error(
      isErrorResponse(responseBody)
        ? responseBody.error
        : `Unable to cancel ticket (${response.status})`,
    );
  }
};

export const getTicket = async (
  accessToken: string,
  id: string,
): Promise<TicketDetails> => {
  const response = await apiFetch(`/api/v1/tickets/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return responseBody as TicketDetails;
};

export const getTicketQr = async (
  accessToken: string,
  id: string,
): Promise<Blob> => {
  const response = await apiFetch(`/api/v1/tickets/${id}/qr-codes`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (response.ok) {
    return await response.blob();
  } else {
    throw new Error("Unable to get ticket QR code");
  }
};

export const validateTicket = async (
  accessToken: string,
  request: TicketValidationRequest,
): Promise<TicketValidationResponse> => {
  const response = await apiFetch(`/api/v1/ticket-validations`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const responseBody = await parseJsonResponse(response);

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return responseBody as Promise<TicketValidationResponse>;
};
