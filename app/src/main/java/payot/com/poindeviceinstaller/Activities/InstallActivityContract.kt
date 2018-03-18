package payot.com.poindeviceinstaller.Activities

/**
 * Created by yongheekim on 2018. 3. 9..
 */
interface InstallActivityContract {

    interface View {

    }

    interface Presenter {
        // 사업자 등록번호(000-00-00000),
        // 금액투입 단위 설정
        // 투입단위, 신호간격, 신호 횟수
        // 기본금액 단위 설정
        fun install(companyNumber: String, coin: Int, signalOffset: Int, signalTime: Int, defaultPrice: Int)
    }
}