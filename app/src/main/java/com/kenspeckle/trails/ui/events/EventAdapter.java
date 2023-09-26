package com.kenspeckle.trails.ui.events;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.kenspeckle.trails.R;
import com.kenspeckle.trails.RetrofitClient;
import com.kenspeckle.trails.dtos.EventDto;
import com.kenspeckle.trails.utils.DateUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
		// We only add dtos that are not already in the list
		for (EventDto dto : eventList) {
			if (!this.eventList.contains(dto)) {
				this.eventList.add(dto);
			}
		}
		this.eventList.sort((o1, o2) -> DateUtils.compareDateTimeString(o1.getStartDate(), o1.getStartTime(), o2.getStartDate(), o2.getStartTime()));
		notifyDataSetChanged();
	}

	public static class EventViewHolder extends RecyclerView.ViewHolder {
		private final TextView title;
		private final TextView teaser;
		private final TextView nbrAttendants;
		private final TextView date;
		private final TextView registrationDeadline;
		private final CheckBox registrationCheckbox;
		private final View itemView;
		private UUID uuid = null;

		public EventViewHolder(@NonNull final View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.event_title);
			teaser = itemView.findViewById(R.id.event_teaser);
			nbrAttendants = itemView.findViewById(R.id.event_nbr_attendants);
			date = itemView.findViewById(R.id.event_date);
			registrationDeadline = itemView.findViewById(R.id.event_registration_deadline);
			registrationCheckbox = itemView.findViewById(R.id.event_registration_checkbox);

			this.itemView = itemView;

		}

		@SuppressLint("SetTextI18n")
		public Void setEventDto(final EventDto eventDto) {
			uuid = eventDto.getId();
			title.setText(eventDto.getTitle());

			teaser.setText(Html.fromHtml(eventDto.getDescription(), Html.FROM_HTML_MODE_COMPACT));

			if (eventDto.getStartDate() == null || eventDto.getStartTime() == null || eventDto.getEndDate() == null || eventDto.getEndTime() == null) {
				System.out.println();
			}
			String startTime = DateUtils.convertSeperateDateAndTimeString(eventDto.getStartDate(), eventDto.getStartTime());
			if (eventDto.getEndDate() != null) {
				String endTime = DateUtils.convertSeperateDateAndTimeString(eventDto.getEndDate(), eventDto.getEndTime());
				date.setText(startTime + " - " + endTime);
			} else {
				date.setText(startTime);
			}


			if (eventDto.isRegistrationEnabled()) {
				setRegistrationEnabled(eventDto);
			} else {
				int nightModeFlags =  itemView.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

				switch (nightModeFlags) {
					case Configuration.UI_MODE_NIGHT_YES -> {
						// Hacky way to reset the background color
						itemView.setBackgroundColor(itemView.getResources().getColor(com.google.android.material.R.color.design_dark_default_color_background, itemView.getContext().getTheme()));
					}
					case Configuration.UI_MODE_NIGHT_NO -> itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white, itemView.getContext().getTheme()));
					case Configuration.UI_MODE_NIGHT_UNDEFINED -> itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white, itemView.getContext().getTheme()));
				}

				nbrAttendants.setVisibility(View.GONE);
				nbrAttendants.setText("");
				registrationDeadline.setVisibility(View.GONE);
				registrationDeadline.setText("");
				registrationCheckbox.setVisibility(View.GONE);
				registrationCheckbox.setText("");
			}
			return null;
		}

		private void setRegistrationEnabled(final EventDto eventDto) {
			nbrAttendants.setVisibility(View.VISIBLE);
			registrationDeadline.setVisibility(View.VISIBLE);

			registrationCheckbox.setVisibility(View.VISIBLE);

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

			registrationDeadline.setText(eventDto.getRegistrationDeadline());
			registrationCheckbox.setOnClickListener(RegistrationUtils.getOnClickListener(itemView.getContext(), this::setEventDto, eventDto));

			if (eventDto.getLoggedInRegistration() != null) {
				itemView.setBackgroundColor(itemView.getResources().getColor(R.color.light_green, itemView.getContext().getTheme()));
				registrationCheckbox.setChecked(true);
				registrationCheckbox.setText(R.string.angemeldet);
			} else if (Objects.equals(eventDto.getMaxAttendees(), eventDto.getCurrentAttendees())) {
				itemView.setBackgroundColor(itemView.getResources().getColor(R.color.light_red, itemView.getContext().getTheme()));
			} else {
				itemView.setBackgroundColor(itemView.getResources().getColor(R.color.enrollment_for_event_possible_light, itemView.getContext().getTheme()));
				registrationCheckbox.setChecked(false);
				registrationCheckbox.setText(R.string.nicht_angemeldet);
			}
		}


	}
}
