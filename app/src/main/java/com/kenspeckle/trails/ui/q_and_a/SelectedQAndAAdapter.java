package com.kenspeckle.trails.ui.q_and_a;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kenspeckle.trails.R;
import com.kenspeckle.trails.dtos.AnswerDto;
import com.kenspeckle.trails.dtos.AuthorDto;
import com.kenspeckle.trails.utils.DateUtils;

import java.util.List;

public class SelectedQAndAAdapter extends RecyclerView.Adapter<SelectedQAndAAdapter.SelectedQAndAViewHolder>{

    private final List<AnswerDto> answerList;

    public SelectedQAndAAdapter(List<AnswerDto> answers) {
        this.answerList = answers;
    }

    @NonNull
    @Override
    public SelectedQAndAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_q_and_a_list_item, parent, false);

        return new SelectedQAndAViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SelectedQAndAViewHolder holder, int position) {
        AnswerDto answerDto = answerList.get(position);
        holder.body.setText(Html.fromHtml(answerDto.getBody(), Html.FROM_HTML_MODE_COMPACT));
        AuthorDto authorDto = answerDto.getAuthor();
        if (authorDto != null) {
            holder.author.setText(authorDto.getName());
        }

        if (answerDto.getAccepted() != null && answerDto.getAccepted()) {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.accepted_question));
        }

        holder.date.setText(DateUtils.convertLocalDateTimeToLocalizedDateTime(answerDto.getCreationDate()));
    }


    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public static class SelectedQAndAViewHolder extends RecyclerView.ViewHolder {

        private final TextView body;
        private final TextView author;
        private final TextView date;

        public SelectedQAndAViewHolder(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.selected_q_and_a_body);
            author = itemView.findViewById(R.id.selected_q_and_a_author);
            date = itemView.findViewById(R.id.selected_q_and_a_date);
        }
    }
}
