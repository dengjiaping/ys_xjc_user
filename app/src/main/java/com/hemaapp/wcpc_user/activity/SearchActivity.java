package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.BaseUtil;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.RecycleUtils;
import com.hemaapp.wcpc_user.adapter.RecommendAdapter;
import com.hemaapp.wcpc_user.adapter.SelectPositionAdapter;
import com.hemaapp.wcpc_user.model.Recomm;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xtom.frame.view.XtomListView;

/**
 */
public class SearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private LinearLayout layout_city;
    private TextView text_city;
    private EditText editText;
    private TextView text_search;
    private XtomListView listView;
    private RecyclerView rvList;

    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;//搜索
    private List<PoiItem> poiItems = new ArrayList<>();

    private SelectPositionAdapter adapter;
    private String citycode;
    private String content;
    private String hint;
    private String city_id;
    private ArrayList<Recomm> recomms = new ArrayList<>();
    private RecommendAdapter recommendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        recommendAdapter = new RecommendAdapter(mContext, recomms);
        RecycleUtils.initVerticalRecyle(rvList);
        rvList.setAdapter(recommendAdapter);
        getNetWorker().recomAddList(city_id);
        if (isNull(citycode))
            citycode = "选择城市";
        text_city.setText(citycode);
        recommendAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                BaseUtil.hideInput(mContext, text_city);
                Recomm item = recomms.get(position);
                if (!isNull(item.getLat())) {
                    mIntent.putExtra("lng", Double.parseDouble(item.getLng()));
                    mIntent.putExtra("lat", Double.parseDouble(item.getLat()));
                    mIntent.putExtra("name", item.getAddress());
                    mIntent.putExtra("city", citycode);
                    setResult(RESULT_OK, mIntent);
                    finish();
                } else {
                    showTextDialog("该数据异常");
                }
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask()   {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(mContext.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 998);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask, HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case RECOM_ADDRESS_LIST:
                @SuppressWarnings("unchecked")
                HemaArrayParse<Recomm> gResult = (HemaArrayParse<Recomm>) baseResult;
                ArrayList<Recomm> goods = gResult.getObjects();
                this.recomms.clear();
                this.recomms.addAll(goods);
                if (recomms.size() == 0) {
                    rvList.setVisibility(View.INVISIBLE);
                } else {
                    rvList.setVisibility(View.VISIBLE);
                }
                recommendAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        layout_city = (LinearLayout) findViewById(R.id.layout);
        text_city = (TextView) findViewById(R.id.tv_city);
        editText = (EditText) findViewById(R.id.ev_content);
        text_search = (TextView) findViewById(R.id.button);
        listView = (XtomListView) findViewById(R.id.listview);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
    }

    @Override
    protected void getExras() {
        citycode = mIntent.getStringExtra("citycode");
        hint = mIntent.getStringExtra("hint");
        city_id = mIntent.getStringExtra("city_id");
        if (isNull(hint))
            hint = "你要去哪";

    }

    @Override
    protected void setListener() {
        editText.setHint(hint);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = s.toString().trim();
                toStartSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0)
                    text_search.setText("搜索");
                else
                    text_search.setText("取消");
            }
        });

        text_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.hideInput(mContext, text_city);
                String value = ((TextView) v).getText().toString();
                if (value.equals("取消"))
                    finish();
                else {
                    content = editText.getText().toString();
                    if (isNull(content)) {
                        showTextDialog("抱歉，请输入搜索内容");
                        return;
                    }

                    toStartSearch();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseUtil.hideInput(mContext, text_city);
                if (adapter != null && !adapter.isEmpty()) {
                    PoiItem item = poiItems.get(position);
                    log_e("lng====" + item.getLatLonPoint().getLongitude());
                    mIntent.putExtra("lng", item.getLatLonPoint().getLongitude());
                    mIntent.putExtra("lat", item.getLatLonPoint().getLatitude());
                    mIntent.putExtra("name", item.getTitle());
                    mIntent.putExtra("city", item.getCityName());
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            }
        });
    }

    private void toStartSearch() {
        String style = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|" +
                "医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
                "金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
        query = new PoiSearch.Query(content, style, citycode);
        /**
         * keyWord表示搜索字符串，
         * 第二个参数表示POI搜索类型，二者选填其一，
         * POI搜索类型共分为以下20种：汽车服务|汽车销售|
         * 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
         * 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
         * 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
         * cityCode表示POI搜索区域的编码，是必须设置参数
         * */
        query.setPageSize(100);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        poiSearch = new PoiSearch(this, query);//初始化poiSearch对象
        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
        poiSearch.searchPOIAsyn();//开始搜索
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    // 取得搜索到的poiitems有多少页
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (isNull(content))
                        poiItems.clear();
                    for (PoiItem poiItem : poiItems) {
                        if (poiItem.getTitle().equals(citycode)) {
                            poiItems.remove(poiItem);
                            break;
                        }
                    }
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (adapter == null) {
                        adapter = new SelectPositionAdapter(mContext, poiItems);
                        adapter.setEmptyString("抱歉，没有搜索到相关信息");
                        listView.setAdapter(adapter);
                    } else {
                        adapter.setItems(poiItems);
                        adapter.setEmptyString("抱歉，没有搜索到相关信息");
                        adapter.notifyDataSetChanged();
                    }
                    if (poiItems.size() == 0) {
                        rvList.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    } else {
                        rvList.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                Toast.makeText(mContext, "该距离内没有找到结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "异常代码---" + i, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case R.id.layout:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
