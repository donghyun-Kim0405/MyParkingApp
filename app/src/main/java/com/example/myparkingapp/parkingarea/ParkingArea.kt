package com.example.myparkingapp.parkingarea

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable



/*'주차장관리번호': '123-1-000008',
'주차장명': '석촌역',
'주차장구분': '공영',
'주차장유형': '노상',
'소재지지번주소': '서울특별시 송파구 송파동 67 ∼ 68',
'주차구획수': '40',
'전화번호': '1899-7275',
'특기사항':
'*면제 - 모범납세자·성실납세증· - 구정유공주민 표지 부착차량*80할인 - 장애인 자가운전차량 - 국가유공상이자 자가운전차량*50할인 - 경차(승요차1000cc미만) - 저공해차량 - 다둥이카드 소지자 (3자녀 이상)*30할인 - 승용차요일제 참여차량   (전자태그 발급차량) - 다둥이카드 소지자 (2자녀)*송파구주차장설치및관리조례에서 정한차량',

'요금정보': '유료',
'주차기본시간': '5',
'주차기본요금': '250',
'추가단위시간': '0',
'추가단위요금': '0',
'1일주차권요금적용시간': '0',
'1일주차권요금': '0',
'월정기권요금': '0',
'결제방법': '카드'


'운영요일': '평일+토요일+공휴일',
'평일운영시작시각': '09:00',
'평일운영종료시각': '21:00',
'토요일운영시작시각': '09:00',
'토요일운영종료시각': '21:00',
'공휴일운영시작시각': '09:00',
'공휴일운영종료시각': '21:00',



'주차기본시간': '5',
'주차기본요금': '250',
'추가단위시간': '0',
'추가단위요금': '0',
'1일주차권요금적용시간': '0',
'1일주차권요금': '0',
'월정기권요금': '0',
'결제방법': '카드'

,*/



data class ParkingArea(
    @SerializedName("주차장관리번호") val parkingAreaManagementNumber:String,
    @SerializedName("주차장명") val parkingAreaName:String,
    @SerializedName("주차장구분") val parkingLotClassification : String,
    @SerializedName("주차장유형") val parkingType:String,
    @SerializedName("소재지도로명주소") val addressByLocationMap:String,
    @SerializedName("소재지지번주소") val locationSupportNumber:String,
    @SerializedName("주차구획수") val numberOfParkingLots:String,
    @SerializedName("급지구분") val landRate:String,
    @SerializedName("부제시행구분") val subtitleImplementationClassification:String,
    @SerializedName("운영요일") val operatingDays:String,
    @SerializedName("평일운영시작시각") val weekdayOperationStartTime:String,
    @SerializedName("평일운영종료시각") val weekdayOperationEndTime:String,
    @SerializedName("토요일운영시작시각") val saturdayOperationStartTime:String,
    @SerializedName("토요일운영종료시각") val saturdayOperationEndTime:String,
    @SerializedName("공휴일운영시작시각") val holidayOperationStartTime:String,
    @SerializedName("공휴일운영종료시각") val holidayOperationEndTime:String,
    @SerializedName("요금정보") val rateInformation:String,
    @SerializedName("주차기본시간") val basicParkingTime:String,
    @SerializedName("주차기본요금") val basicParkingFee:String,
    @SerializedName("추가단위시간") val additionalUnitTime:String,
    @SerializedName("추가단위요금") val additionalCharge:String,
    @SerializedName("1일주차권요금적용시간") val applicationTimeForParkingTicketPerDay:String,
    @SerializedName("1일주차권요금") val oneDayParkingTicketFee:String,
    @SerializedName("월정기권요금") val monthlyTicketFee:String,
    @SerializedName("결제방법") val paymentMethod:String,
    @SerializedName("특기사항") val specialNote:String,
    @SerializedName("관리기관명") val nameOfManagementAgency:String,
    @SerializedName("전화번호") val phoneNumber:String,
    @SerializedName("위도") val latitude:Double,
    @SerializedName("경도") val longitude:Double,
    @SerializedName("데이터기준일자") val dataBaseDate:String,
    @SerializedName("제공기관코드") val agencyCode:String,
    @SerializedName("제공기관명") val nameOfProvider:String
):Serializable