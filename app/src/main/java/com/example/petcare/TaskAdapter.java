package com.example.petcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.petcare.R;
import com.example.petcare.db.Task;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private final OnTaskActionListener listener;
    private final Context context;

    public interface OnTaskActionListener {
        void onMarkDone(Task task);
        void onEdit(Task task);
        void onDelete(Task task);
    }

    public TaskAdapter(Context context, OnTaskActionListener listener) {
        super(diffCallback());
        this.context = context;
        this.listener = listener;
        private static DiffUtil.ItemCallback<Task> diffCallback() {
            return new DiffUtil.ItemCallback<Task>() {
                @Override
                public boolean areItemsTheSame(Task oldItem, Task newItem) {
                    return oldItem.id == newItem.id;
                }
                @Override
                public boolean areContentsTheSame(Task oldItem, Task newItem) {
                    return oldItem.title.equals(newItem.title) &&
                            oldItem.done == newItem.done;
                }
            };
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = getItem(position);
            holder.bind(task);
        }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            private final android.widget.TextView tvTitle, tvDue;
            private final android.widget.CheckBox cbDone;

            public TaskViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTaskTitle);
                tvDue = itemView.findViewById(R.id.tvDueDate);
                cbDone = itemView.findViewById(R.id.cbDone);
            }

            public void bind(Task task) {
                tvTitle.setText(task.title);
                cbDone.setChecked(task.done);
                // Simple due date display
                if (task.dueMillis > 0) {
                    long diff = task.dueMillis - System.currentTimeMillis();
                    int days = (int) (diff / (1000 * 60 * 60 * 24));
                    tvDue.setText(days > 0 ? "Due in " + days + "d" : "Overdue");
                }
                cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    task.done = isChecked;
                    // We'll re-emit; adapter diff will handle update
                    listener.onMarkDone(task);
                });
                itemView.setOnClickListener(v -> listener.onEdit(task));
                itemView.setOnLongClickListener(v -> {
                    listener.onDelete(task);
                    return true;
                });
            }
        }
    }