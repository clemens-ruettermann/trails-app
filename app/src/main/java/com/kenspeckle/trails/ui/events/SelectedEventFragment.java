package com.kenspeckle.trails.ui.events;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kenspeckle.trails.databinding.FragmentSelectedEventBinding;
import com.kenspeckle.trails.dtos.EventDto;
import com.kenspeckle.trails.utils.DateUtils;

import java.io.Serializable;

public class SelectedEventFragment extends Fragment {
	private FragmentSelectedEventBinding binding;

	private EventDto eventDto;

	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Serializable eventDto = getArguments().getSerializable("eventDto");
		if (eventDto != null) {
			this.eventDto = (EventDto) eventDto;
		}
	}

	@SuppressLint("SetTextI18n")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = FragmentSelectedEventBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

//		EventDto = EventAdapter.EventList.get(EventAdapter.currentPos);

		binding.selectedEventTitle.setText(eventDto.getTitle());
		binding.selectedEventBody.setText(Html.fromHtml(eventDto.getDescription(), Html.FROM_HTML_MODE_COMPACT));
		binding.selectedEventAuthor.setText(eventDto.getAuthor().getName());
		String startTime = DateUtils.convertSeperateDateAndTimeString(eventDto.getStartDate(), eventDto.getStartTime());
		String endTime = DateUtils.convertSeperateDateAndTimeString(eventDto.getEndDate(), eventDto.getEndTime());
		binding.selectedEventDate.setText(startTime + " - " + endTime);

		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
