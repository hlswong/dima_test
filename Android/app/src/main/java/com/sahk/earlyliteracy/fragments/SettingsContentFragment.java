package com.sahk.earlyliteracy.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;

public class SettingsContentFragment extends BaseFragment {

    public static SettingsContentFragment newInstance(int contentType) {
        Bundle bundle = new Bundle();
        bundle.putInt("contentType", contentType);
        SettingsContentFragment settingsContentFragment = new SettingsContentFragment();
        settingsContentFragment.setArguments(bundle);
        return settingsContentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_settingscontent, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_SETTINGS);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.setBackgroundColor(0);
        webView.getBackground().setAlpha(0);
        Resources res = getResources();
        float desity =  res.getDisplayMetrics().density;
        float width = res.getDisplayMetrics().widthPixels;
        float scaledWidth = width/desity;

        Log.d("fonts","desity: " + desity + " ,  width: " + width + " , sw: " + scaledWidth);
        ////float fontSize = res.getDimension(R.dimen.web_font_medium_sp);

        float fontSize = 0.0f;
        Integer fontNum = sharedPreferences.getInt("fontSize", 0);
        if ( fontNum == 14 ){
            fontSize = res.getDimension(R.dimen.web_font_small_sp);
        }else if ( fontNum == 18 ){
            fontSize = res.getDimension(R.dimen.web_font_medium_sp);
        }else if ( fontNum == 24 ){
            fontSize = res.getDimension(R.dimen.web_font_large_sp);
        }else {
            //Commented By Rex
//            fontSize = res.getDimension(R.dimen.normal_dp);
            //Added By Rex
            fontSize = res.getDimension(R.dimen.web_font_medium_sp);
        }
        fontSize = fontSize* (scaledWidth/640)*(scaledWidth/640);

        webView.getSettings().setDefaultFontSize((int)fontSize);
        switch (getArguments().getInt("contentType")) {
            case 0:
                webView.loadUrl("file:///android_asset/sahk.html");
                webView.setContentDescription("香港耀能協會創立於1963年前稱 香港痙攣協會  為不同年齡的腦麻痺人士提供康復服務 後來因應社會需要 協會為各類身體殘障人士開創多項服務 協會遂於2008年改名為 香港耀能協會  以 耀承所授  卓越展能 為信念 為殘疾人士提供多元化的康復服務 發展他們的潛能 提升其獨立能力和自信 協助他們融入社會 協會所提供的服務分為四個核心範疇 包括 兒童及家庭支援服務   特殊教育服務   成人服務 及 社區支援服務  並附以多個專項服務計劃 現時協會轄下共67個服務單位及專項計劃 為逾15,000個家庭提供常規服務 另為社區人士及學校學童提供康復服務 電話 2527 8978 傳真 2866 3727 地址 香港北角百福道21號17樓 電郵 ho@sahk1963.org.hk 網站 www.sahk1963.org.hk");
                break;
            case 1:
                webView.loadUrl("file:///android_asset/privacy.html");
                webView.setContentDescription("私隱政策 香港耀能協會在提供 學前語文秘笈 流動程式時 不會收集任何個人資料 並遵守香港特別行政區現行頒佈的 個人資料 私隱 條例 有關條文  學前語文秘笈 流動程式設有 分享 功能 此功能乃透過第三者服務供應商進行 請閣下在使用第三者服務供應商的 分享 服務時 留意該第三者服務供應商對有關服務的私隱政策 例如本程式連接到Facebook社交網站時 該網站可能會向本程式釋放閣下在有關網站上的設定資料 而令本程式收集到閣下部份的個人資料 有關資料包括但不限於閣下的基本資料 本程式收集此等個人資料僅為閣下提供社交網站連接的功能 本程式由社交網站收集的個人資料 將在閣下登出有關社交網站時立即被本程式刪除  學前語文秘笈 流動程式設有存取使用者的遊戲次數及遊戲分數等紀錄的統計功能 有關紀錄完全不牽涉任何可辨識使用者身分的資料 >為提供最有效的程式服務  學前語文秘笈 流動程式設有收集閣下流動裝置的部份資料的功能 例如設備識別 移動設備型號 生產商 IP地址 所使用的操作系統及版本等 並使用於不同地方 設備及連接中等功能  學前語文秘笈 流動程式為得以在閣下的流動裝置上正常運作 本程式會讀取閣下流動裝置內的SD卡 並修改或刪除本程式儲存於SD卡內的內容 如香港耀能協會因為個別原因而需要透過 學前語文秘笈 流動程式收集個人資料 例如 電郵地址  將有特別通知邀請閣下自願提供資料 本會邀請閣下提供資料時 會另行列明收集資料的目的和用途 本會承諾遵循 個人資料 私隱 條例 的規定 嚴格保障用戶提供予本程式之個人資料的安全和保密 ");
                break;
            case 2:
                webView.loadUrl("file:///android_asset/disclaimer.html");
                webView.setContentDescription("注意事項 長時間或過度使用流動裝置有機會對幼兒的視力 聽力 專注力 關節骨骼 肌肉等構成不良影響 並有可能出現成癮的問題 家長宜陪同幼兒使用本流動程式 避免對幼兒發展構成不良影響 免責聲明  學前語文秘笈 流動程式之資訊及內容的應用以中華人民共和國香港特別行政區為基礎範圍 所列條款乃按照香港特別行政區法律詮釋及本協議受其法律規管 即資訊及內容未必符合其他地區的需要及規範 或未必切合其他地區的實際情況 使用者同意接受香港法院的非專屬司法管轄權的管轄 任何使用 學前語文秘笈 流動程式的人士 必須清楚瞭解香港耀能協會不承擔任何因使用 學前語文秘笈 流動程式或其包含的任何內容所引起的直接或間接的損失或損壞 可預見的或其他包括間接 相應而生的 特別或懲罰性的損害 使用者必須明白使用本程式及其內容均需自行承擔一切風險 香港耀能協會承諾力求程式內容之準確性及完整性 但如有錯誤或遺漏 本會並不承擔任何賠償責任 所有在本程式內刊載的資料僅作為參考之用 除註明 學前語文秘笈 流動程式製作之內容外 其他任何內容 香港耀能協會一概不承擔因意外 包括感染電腦病毒  誹謗 侵犯版權或知識產權造成的損失 使用者應自行決定是否使用及下載本程式之任何資料 並承擔風險 如因下載本程式的資料而導致流動裝置之任何損壞或資料流失 皆由使用者自行負責 基於以下原因而造成之損失 包括但不限於利潤 商譽 使用 資料損失或其他無形損失 香港耀能協會不承擔任何直接 間接 附帶 特別 衍生性或懲罰性之責任   即使本會事前已被告知賠償之可能性亦然      a  本程式之使用或無法使用   b  經由或透過本程式取得之任何資料 資訊或服務 或接收之訊息 或取得資料所衍生之成本   c  傳輸或資料遭未獲授權的存取或變更   d  任何第三者之聲明或行為 或  e  本程式其他相關事宜  學前語文秘笈 流動程式或會載有由第三方提供的資訊 提供或協助提供該等由第三方所給予的資訊或外界網站超連結 並不構成香港耀能協會贊同或沒有不贊同任何該等資訊之內容或外界網站之內容的任何明示或暗示的聲明 陳述或保證 對任何因使用或不當使用或不能使用或依據由或通過本程式傳遞或提供的任何該等資訊之內容或外界網站的資訊之內容而引致或所涉及的損失 毀壞或損害 包括但不限於相應而生的損失 毀壞或損害  本會概不承擔任何法律責任 包括疏忽責任  義務或責任  學前語文秘笈 是一個免費程式 但電話網絡服務供應商會向閣下收取使用數據的費用 此等費用於漫遊時可能會十分昂貴 請確保閣下智能電話上的設定已把 資料漫遊 選項關上 對於因使用本程式而需向第三方繳付的任何費用 本會概不承擔任何法律責任 包括疏忽責任  義務或責任 香港耀能協會可隨時停止或變更有關條款而毋須事前通知  ");
                break;
            case 3:
                /////webView.loadUrl("file:///android_asset/parent.html");
                break;
            case 4:
                webView.loadUrl("file:///android_asset/browse.html");
                webView.setContentDescription("無障礙瀏覽  學前語文秘笈 流動程式根據萬維網聯盟  W3C  無障礙網頁內容指引  WCAG  2.0 AA級別而編寫 如閣下有任何查詢 或於使用此程式時遇到任何困難或障礙 請電郵至 ho@sahk1963.org.hk ");
                break;
            case 5:
                webView.loadUrl("file:///android_asset/copyright.html");
                webView.setContentDescription("版權及知識產權 使用者對 學前語文秘笈 流動程式或其中提供的資訊無任何權利  學前語文秘笈 流動程式內的所有內容 包括但不限於文字 圖像 相片及影音等之版權 除特別指明外 均為香港耀能協會所有 除特別指明外 任何人士未經香港耀能協會的書面同意 不得以任何方式抄襲 更改 複印 出版 上載 傳送及發放上述資訊及資訊 用戶如發現其他人士或組織涉及侵犯 學前語文秘笈 流動程式版權 請立即與本會聯絡 如欲使用 學前語文秘笈 流動程式的資訊作個人用途或非商業性質的內部用途 必須事前獲得香港耀能協會的明文批准 如欲作出申請 請電郵至 ho@sahk1963.org.hk  ");
                break;
        }
        return view;
    }
}