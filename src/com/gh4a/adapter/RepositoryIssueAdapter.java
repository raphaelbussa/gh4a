/*
 * Copyright 2011 Azwan Adli Abdullah
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gh4a.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gh4a.Gh4Application;
import com.gh4a.R;
import com.gh4a.utils.AvatarHandler;
import com.gh4a.utils.IntentUtils;
import com.gh4a.utils.StringUtils;

import org.eclipse.egit.github.core.RepositoryIssue;

public class RepositoryIssueAdapter extends RootAdapter<RepositoryIssue> implements
        View.OnClickListener {
    public RepositoryIssueAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_issue, parent, false);
        Gh4Application app = (Gh4Application) mContext.getApplicationContext();
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.ivGravatar = (ImageView) v.findViewById(R.id.iv_gravatar);
        viewHolder.ivGravatar.setOnClickListener(this);

        viewHolder.tvDesc = (TextView) v.findViewById(R.id.tv_desc);
        viewHolder.tvDesc.setTypeface(app.boldCondensed);

        viewHolder.tvExtra = (TextView) v.findViewById(R.id.tv_extra);
        viewHolder.tvNumber = (TextView) v.findViewById(R.id.tv_number);
        viewHolder.llLabels = (LinearLayout) v.findViewById(R.id.ll_labels);
        viewHolder.ivAssignee = (ImageView) v.findViewById(R.id.iv_assignee);
        viewHolder.tvComments = (TextView) v.findViewById(R.id.tv_comments);
        viewHolder.tvRepo = (TextView) v.findViewById(R.id.tv_repo);
        viewHolder.tvRepo.setVisibility(View.VISIBLE);
        viewHolder.tvMilestone = (TextView) v.findViewById(R.id.tv_milestone);

        v.setTag(viewHolder);
        return v;
    }

    @Override
    protected void bindView(View v, RepositoryIssue issue) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();

        AvatarHandler.assignAvatar(viewHolder.ivGravatar, issue.getUser());
        viewHolder.ivGravatar.setTag(issue);
        viewHolder.tvNumber.setText(String.valueOf(issue.getNumber()));

        IssueAdapter.makeLabelBadges(viewHolder.llLabels, issue.getLabels());

        String userName = issue.getUser() != null
                ? issue.getUser().getLogin() : mContext.getString(R.string.deleted);

        viewHolder.tvDesc.setText(issue.getTitle());
        viewHolder.tvExtra.setText(userName + "\n"
                + StringUtils.formatRelativeTime(mContext, issue.getCreatedAt(), true));

        if (issue.getComments() > 0) {
            viewHolder.tvComments.setVisibility(View.VISIBLE);
            viewHolder.tvComments.setText(String.valueOf(issue.getComments()));
        } else {
            viewHolder.tvComments.setVisibility(View.GONE);
        }

        viewHolder.tvRepo.setText(mContext.getString(R.string.repo_issue_on,
                issue.getRepository().getOwner().getLogin() + "/" + issue.getRepository().getName()));

        if (issue.getAssignee() != null) {
            viewHolder.ivAssignee.setVisibility(View.VISIBLE);
            AvatarHandler.assignAvatar(viewHolder.ivAssignee, issue.getAssignee());
        } else {
            viewHolder.ivAssignee.setVisibility(View.GONE);
        }

        if (issue.getMilestone() != null) {
            viewHolder.tvMilestone.setVisibility(View.VISIBLE);
            viewHolder.tvMilestone.setText("Milestone : " + issue.getMilestone().getTitle());
        } else {
            viewHolder.tvMilestone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_gravatar) {
            RepositoryIssue issue = (RepositoryIssue) v.getTag();
            mContext.startActivity(IntentUtils.getUserActivityIntent(mContext, issue.getUser()));
        }
    }

    private static class ViewHolder {
        public ImageView ivGravatar;
        public TextView tvDesc;
        public TextView tvExtra;
        public TextView tvNumber;
        public LinearLayout llLabels;
        public ImageView ivAssignee;
        public TextView tvComments;
        public TextView tvRepo;
        public TextView tvMilestone;
    }
}
