package com.kenspeckle.trails.ui.events;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.kenspeckle.trails.R;
import com.kenspeckle.trails.dtos.EventDto;
import com.kenspeckle.trails.utils.DateUtils;

import java.util.List;
import java.util.Objects;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<EventDto> eventList;

    public EventAdapter(List<EventDto> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.setEventDto(eventList.get(position));
        holder.itemView.setOnClickListener(v -> {
            EventFragmentDirections.ActionNavEventsToSelectedEventFragment action = EventFragmentDirections.actionNavEventsToSelectedEventFragment(eventList.get(position));
            Navigation.findNavController(v).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addElementsAndSort(List<EventDto> eventList) {
        this.eventList.addAll(eventList);
        this.eventList.sort((o1, o2) -> DateUtils.compareDateTimeString(o1.getStartDate(), o1.getStartTime(), o2.getStartDate(), o2.getStartTime()));
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView teaser;
        private final TextView nbrAttendants;
        private final TextView date;

        private final View itemView;

        public EventViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            teaser = itemView.findViewById(R.id.event_teaser);
            nbrAttendants = itemView.findViewById(R.id.event_nbr_attendants);
            date = itemView.findViewById(R.id.event_date);
            this.itemView = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void setEventDto(EventDto eventDto) {
            title.setText(eventDto.getTitle());

            teaser.setText(Html.fromHtml(eventDto.getDescription(), Html.FROM_HTML_MODE_COMPACT));

            String startTime = DateUtils.convertSeperateDateAndTimeString(eventDto.getStartDate(), eventDto.getStartTime());
            String endTime = DateUtils.convertSeperateDateAndTimeString(eventDto.getEndDate(), eventDto.getEndTime());
            date.setText(startTime + " - " + endTime);


            if (eventDto.isRegistrationEnabled()) {
                StringBuilder sb = new StringBuilder();

                if (eventDto.getCurrentAttendees() != null) {
                    sb.append(eventDto.getCurrentAttendees());
                } else {
                    sb.append(eventDto.getCurrentAttendees());
                }
                sb.append(" / ");
                if (eventDto.getMaxAttendees() != null) {
                    sb.append(eventDto.getMaxAttendees());
                } else {
                    sb.append(eventDto.getMaxAttendees());
                }
                sb.append(" angemeldet");
                nbrAttendants.setText(sb.toString());

                if (eventDto.getLoggedInRegistration() != null) {
                    itemView.setBackgroundColor(itemView.getResources().getColor(R.color.enrolled_event));
                } else if (Objects.equals(eventDto.getMaxAttendees(), eventDto.getCurrentAttendees())) {
                    itemView.setBackgroundColor(itemView.getResources().getColor(R.color.enrollment_for_event_not_possible));
                } else {
                    itemView.setBackgroundColor(itemView.getResources().getColor(R.color.enrollment_for_event_possible));
                }
            } else {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));
                nbrAttendants.setText("");
            }
        }
    }
}
