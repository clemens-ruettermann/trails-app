package com.kenspeckle.trails.ui.q_and_a;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kenspeckle.trails.databinding.FragmentSelectedQAndABinding;
import com.kenspeckle.trails.dtos.QAndADto;
import com.kenspeckle.trails.utils.DateUtils;

import java.io.Serializable;
import java.util.List;

public class SelectedQAndAFragment extends Fragment {

	private FragmentSelectedQAndABinding binding;

	private QAndADto qAndADto;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Serializable QAndADto = getArguments().getSerializable("qAndADto");
		if (QAndADto != null) {
			this.qAndADto = (QAndADto) QAndADto;
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		binding = FragmentSelectedQAndABinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		binding.selectedQAndATitle.setText(qAndADto.getTitle());
		binding.selectedQAndABody.setText(Html.fromHtml(qAndADto.getBody(), Html.FROM_HTML_MODE_COMPACT));
		binding.selectedQAndAAuthor.setText(qAndADto.getAuthor().getName());
		binding.selectedQAndADate.setText(DateUtils.convertLocalDateTimeToLocalizedDateTime(qAndADto.getCreationDate()));

		RecyclerView recyclerView = binding.selectedQAndARecyclerView;
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
		if (qAndADto.getAnswers() != null && !qAndADto.getAnswers().isEmpty()) {
			recyclerView.setAdapter(new SelectedQAndAAdapter(qAndADto.getAnswers()));
		} else {
			recyclerView.setAdapter(new QAndAAdapter(List.of()));
		}
		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
