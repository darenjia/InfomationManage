package com.bokun.bkjcb.infomationmanage.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Adapter.RecAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleItemCallback;
import com.bokun.bkjcb.infomationmanage.Adapter.TagAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hhl.library.FlowTagLayout;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener {
    public static SecondFragment fragment;
    private RecyclerView recyclerView;
    private RecAdapter adapter;
    private int flag = 0;
    private int flag1 = 0;
    private int layer;
    private TagAdapter tagAdapter;
    private ArrayList<String> strings;

    public static SecondFragment newInstance() {
        if (fragment == null) {
            fragment = new SecondFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_two, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView btn_return = (TextView) view.findViewById(R.id.ret_btn);
        TextView btn_home = (TextView) view.findViewById(R.id.home_btn);
        FlowTagLayout tagLayout = (FlowTagLayout) view.findViewById(R.id.flow_layout);
        tagAdapter = new TagAdapter(getContext());
        strings = new ArrayList<>();
        tagLayout.setAdapter(tagAdapter);
        btn_home.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        ArrayList<Level> list = getFirstData();
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecAdapter(getContext(), list);

        recyclerView.setAdapter(adapter);
        return view;
    }

    private ArrayList<Level> getFirstData() {
        ArrayList<Level> list = new ArrayList<>();
        Level level1 = new Level();
        level1.setDepartmentNameA("管理单位");
        list.add(level1);
        Level level2 = new Level();
        level2.setDepartmentNameA("企业单位");
        level2.setLevel(2);
        list.add(level2);
        return list;
    }

    private ArrayList<Level> getSecondData() {
        ArrayList<Level> list = new ArrayList<>();
        Level level1 = new Level();
        level1.setDepartmentNameA("市级单位");
        level1.setLevel(0);
        list.add(level1);
        Level level2 = new Level();
        level2.setDepartmentNameA("区级单位");
        level2.setLevel(1);
        list.add(level2);
        return list;
    }

    @Override
    protected void setListener() {
        adapter.setItemClick(new SimpleItemCallback<Level, RecAdapter.MyViewHolder>() {
            @Override
            public void onItemClick(int position, Level model, int tag) {
                switch (layer) {
                    case 0:
                        if (position == 0) {
                            adapter.setData(getSecondData());
                        } else {
                            adapter.setData(getData(2, -1, -1, -1, -1));
                        }
                        setStrings(model.getDepartmentNameA());
                        layer = 1;
                        flag = position;
                        break;
                    case 1:
                        if (flag == 0) {
                            if (position == 0) {
                                adapter.setData(getData(0, -1, -1, -1, -1));
                            } else {
                                adapter.setData(getData());
                            }
                        } else {
                            adapter.setData(getData(2, -1, -1, -1, -1));
                        }
                        layer = 2;
                        flag1 = position;
                        setStrings(model.getDepartmentNameA());
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
            }
        });
    }

    private ArrayList<Level> getData(int level, int kind1, int kind2, int kind3, int quxian) {
        return DBManager.newInstance(getContext()).queryLevel(level, kind1, kind2, kind3, quxian);
    }

    private ArrayList<Level> getData() {
        return DBManager.newInstance(getContext()).queryAllQuName();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ret_btn:
                if (layer == 1) {
                    adapter.setData(getFirstData());
                    layer = 0;
                }else if (layer ==2){
                    if (flag == 1){

                    }
                }
                break;
            case R.id.home_btn:
                adapter.setData(getFirstData());
                layer = 0;
                break;
        }
    }

    private void setStrings(String s) {
        if (s == null) {
            strings.remove(strings.size() - 1);
        } else {
            strings.add(s);
        }
        tagAdapter.onlyAddAll(strings);
    }
}
