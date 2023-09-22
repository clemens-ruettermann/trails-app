package com.kenspeckle.trails.ui.q_and_a;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.kenspeckle.trails.R;
import com.kenspeckle.trails.dtos.AuthorDto;
import com.kenspeckle.trails.dtos.QAndADto;
import com.kenspeckle.trails.utils.DateUtils;

import java.util.List;

public class QAndAAdapter extends RecyclerView.Adapter<QAndAAdapter.QAndAViewHolder> {

	private final List<QAndADto> qAndAList;

	public QAndAAdapter(List<QAndADto> qAndAList) {
		this.qAndAList = qAndAList;
	}

	@NonNull
	@Override
	public QAndAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.q_and_a_list_item, parent, false);

		return new QAndAViewHolder(view);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull QAndAViewHolder holder, int position) {
		QAndADto qAndADto = qAndAList.get(position);
		holder.title.setText(qAndADto.getTitle());
		holder.teaser.setText(Html.fromHtml(qAndADto.getBody(), Html.FROM_HTML_MODE_COMPACT));
		holder.category.setText(qAndADto.getCategory());
		holder.nbrAnswers.setText(Integer.toString(qAndADto.getAnswers().size()));
		AuthorDto authorDto = qAndADto.getAuthor();
		if (authorDto != null) {
			holder.author.setText(authorDto.getName());
		}

		holder.date.setText(DateUtils.convertLocalDateTimeToLocalizedDateTime(qAndADto.getCreationDate()));

		holder.itemView.setOnClickListener(v -> {
			QAndAFragmentDirections.ActionNavNewsToSelectedQAndAFragment action = QAndAFragmentDirections.actionNavNewsToSelectedQAndAFragment(qAndAList.get(position));
			Navigation.findNavController(v).navigate(action);
		});
	}

	public void addElementsAtEnd(List<QAndADto> qAndAList) {
		this.qAndAList.addAll(qAndAList);
		notifyItemRangeInserted(this.qAndAList.size(), qAndAList.size());
	}

	@Override
	public int getItemCount() {
		return qAndAList.size();
	}

	public static class QAndAViewHolder extends RecyclerView.ViewHolder {

		private final TextView title;
		private final TextView teaser;
		private final TextView category;
		private final TextView nbrAnswers;
		private final TextView author;
		private final TextView date;

		public QAndAViewHolder(@NonNull View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.q_and_a_title);
			teaser = itemView.findViewById(R.id.q_and_a_teaser);
			category = itemView.findViewById(R.id.q_and_a_category);
			nbrAnswers = itemView.findViewById(R.id.q_and_a_nbr_answers);
			author = itemView.findViewById(R.id.q_and_a_author);
			date = itemView.findViewById(R.id.q_and_a_date);
		}
	}
}
