package com.kenspeckle.trails.ui.news;

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
import com.kenspeckle.trails.dtos.NewsDto;
import com.kenspeckle.trails.ui.news.NewsFragmentDirections;
import com.kenspeckle.trails.utils.DateUtils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
	private final List<NewsDto> newsList;

	public NewsAdapter(List<NewsDto> newsList) {
		this.newsList = newsList;
	}

	@NonNull
	@Override
	public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
		return new NewsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
		holder.setNewsDto(newsList.get(position));
		holder.itemView.setOnClickListener(v -> {
			NewsFragmentDirections.ActionNavNewsToSelectedNewsFragment action = NewsFragmentDirections.actionNavNewsToSelectedNewsFragment(newsList.get(position));
			Navigation.findNavController(v).navigate(action);
		});
	}

	@Override
	public int getItemCount() {
		return newsList.size();
	}

	public void addElementsAtEnd(List<NewsDto> newsList) {
		this.newsList.addAll(newsList);
		notifyItemRangeInserted(this.newsList.size(), newsList.size());
	}

	public static class NewsViewHolder extends RecyclerView.ViewHolder {
		private final TextView title;
		private final TextView teaser;
		private final TextView author;
		private final TextView date;

		public NewsViewHolder(@NonNull final View itemView) {
			super(itemView);

			title = itemView.findViewById(R.id.news_title);
			teaser = itemView.findViewById(R.id.news_teaser);
			author = itemView.findViewById(R.id.news_author);
			date = itemView.findViewById(R.id.news_date);
		}

		public void setNewsDto(NewsDto newsDto) {
			title.setText(newsDto.getTitle());

			teaser.setText(Html.fromHtml(newsDto.getTeaser(), Html.FROM_HTML_MODE_COMPACT));
			AuthorDto authorDto = newsDto.getAuthor();
			if (authorDto != null) {
				author.setText(authorDto.getName());
			}

			date.setText(DateUtils.convertLocalDateTimeToLocalizedDateTime(newsDto.getCreationDate()));

		}
	}
}
