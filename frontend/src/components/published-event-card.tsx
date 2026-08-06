import { PublishedEventSummary } from "@/domain/domain";
import { Card } from "./ui/card";
import { Calendar, Heart, MapPin, Share2 } from "lucide-react";
import { format } from "date-fns";
import { Link } from "react-router";
import RandomEventImage from "./random-event-image";

interface PublishedEventCardProperties {
  publishedEvent: PublishedEventSummary;
}

const PublishedEventCard: React.FC<PublishedEventCardProperties> = ({
  publishedEvent,
}) => {
  return (
    <Link className="block h-full" to={`/events/${publishedEvent.id}`}>
      <Card className="h-full w-full overflow-hidden py-0 gap-2">
        {/* Card Image */}
        <div className="h-48 md:h-55">
          <RandomEventImage />
        </div>
        <div className="px-4 pt-2">
          <h3 className="min-h-14 text-xl font-medium leading-7">
            {publishedEvent.name}
          </h3>
        </div>
        <div className="flex flex-1 flex-col px-4 pb-2">
          <div className="mb-3 flex min-h-12 gap-3 text-base text-gray-500">
            <MapPin className="w-6 shrink-0" /> {publishedEvent.venue}
          </div>
          <div className="mb-3 flex min-h-12 gap-3 text-base text-gray-500">
            {publishedEvent.start && publishedEvent.end ? (
              <div className="flex gap-3">
                <Calendar className="w-6 shrink-0" />{" "}
                {format(publishedEvent.start, "PP")} -{" "}
                {format(publishedEvent.end, "PP")}
              </div>
            ) : (
              <div className="flex gap-3">
                <Calendar />
                Dates TBD
              </div>
            )}
          </div>
          <div className="mt-auto flex justify-between border-t p-3 text-gray-500">
            <button className="cursor-pointer">
              <Heart />
            </button>
            <button className="cursor-pointer">
              <Share2 />
            </button>
          </div>
        </div>
      </Card>
    </Link>
  );
};

export default PublishedEventCard;
