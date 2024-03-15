

# AllergySafe - 알레르기 종합 관리 플랫폼

<div align='center'>
<img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/69a4e243-dd59-4db3-8557-b5b13ac65698" width="320px" height="320px" style="display: inline-block" />
<p>&nbsp;</p>
<img src='https://img.shields.io/badge/Version-1.0.0-blue?style=for-the-badge&logo'>
</a>  
</div>

<div align='center'>

---  
  
### Quick Links

<a href='https://allergysafe.life'>
<img src='https://img.shields.io/badge/HOMEPAGE-gray?style=for-the-badge'>
</a>
  
<a href='https://youtu.be/819ZENij3T4'>
<img src='https://img.shields.io/badge/VIDEO-blue?style=for-the-badge'>
</a>
  	
<a href='https://allergysafe.life/api/swagger/swagger-ui/index.html'>
<img src='https://img.shields.io/badge/OPEN API-black?style=for-the-badge'>
</a>
	
</div>

---

## :book: 목차 (Table of Contents)
<details open="open">
  <ol>
    <li><a href="#about-the-project"> ➤ 프로젝트 소개 (Intro)</a></li>
    <li><a href="#features"> ➤ 기능 설명 (Features)</a></li>
      <ul>
        <li><a href="#feature1">알레르기 프로필 (Allergy Profile)</a></li>
        <li><a href="#feature2">알레르기 일기장 (Allergy Diary)</a></li>
		<li><a href="#feature3">바코드 스캐너 (Barcode Scanner)</a></li>
        <li><a href="#feature4">알레르기 항원 추론 (Allergy Antigen Inference)</a></li>
      </ul>
    <li><a href="#prerequisites"> ➤ 컴퓨터 구성 / 필수 조건 안내 (Prequisites)</a></li>
    <li><a href="#techniques"> ➤ 기술 스택 (Techniques Used)</a></li>
    <li><a href="#team"> ➤ 팀 정보 (Team Information)</a></li>
  </ol>
</details>

<h2 id="about-the-project"> :monocle_face: 프로젝트 소개 (Intro)</h2>

> ‘Allergy Safe’는 바코드 인식을 통해 식품 및 의약품의 원재료를 불러와 알레르기 원인을 추론하고 공유, 예방하는 종합적인 알레르기 관리 서비스입니다.


<h2 id="features"> :plate_with_cutlery: 기능 설명 (Features)</h2>

**4가지 핵심기능**은 다음과 같습니다.

* **`알레르기 프로필 (Allergy Profile)`**: 본인 또는 가족의 알레르기 유발 식품 및 의약품 목록을 등록할 수 있는 프로필 기능입니다.
* **`알레르기 일기장 (Allergy Diary)`** : 섭취한 식품 및 복용한 의약품과 발생한 알레르기 증상을 기록할 수 있는 일기장 기능입니다.
* **`바코드 스캐너 (Barcode Scanner)`** : 식품 및 의약품의 바코드를 카메라로 인식하여 원재료 목록을 자동으로 불러오는 기능입니다.
* **`알레르기 항원 추론 (Allergy Antigen Inference)`** : 알레르기 프로필과 일기장으로부터 알레르기 항원을 추론할 수 있는 기능입니다.

---
<h3 id="feature1">알레르기 프로필 (Allergy Profile)</h3>

> 본인 또는 가족의 알레르기 유발 식품 및 의약품 목록을 등록할 수 있는 프로필 기능입니다.

<div>
  <img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/2ddab26b-c485-4cd7-b624-9f6de768dfc7" width="24%" style="display: block;"/>
  <img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/e979bcd2-09ce-41c1-81ef-e50714328201" width="24%" style="display: block;"/>
  <img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/9ba2dfdc-2472-41ea-88bc-08a9408748a8" width="24%" style="display: block;"/>
</div>

- 프로필 생성 : 본인 및 타인의 알레르기 정보를 등록할 수 있음
- 프로필 수정 및 삭제 : 본인 및 타인의 프로필을 수정 및 삭제할 수 있음
- 프로필 공유 : 본인의 프로필을 타인에게 링크로 공유할 수 있음


<h3 id="feature2">알레르기 일기장 (Allergy Diary)</h3>

> 섭취한 식품 및 복용한 의약품과 발생한 알레르기 증상을 기록할 수 있는 일기장 기능입니다.

<div>
  <img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/4c844a59-6d14-47fd-a445-2859a79a8e70" width="24%" style="display: block;"/>
  <img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/e2bf5b20-b6d0-4c88-876e-b728b7a417ec" width="24%" style="display: block;"/>
</div>

- 식품 및 의약품 기록 : 바코드 스캐너를 이용하면 자동으로 섭취한 식품과 복용한 의약품이 기록되며 바코드가 존재하지 않는 식품의 경우 수동으로 추가 가능함
- 알레르기 반응 기록 : 사용자가 알레르기 증상이 나타났을 때 직접 어떠한 반응이 나타났는지 기록할 수 있음

<h3 id="feature3">바코드 스캐너 (Barcode Scanner)</h3>

> 식품 및 의약품의 바코드를 카메라로 인식하여 원재료 목록을 자동으로 불러오는 기능입니다.

<div>
  <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/9fd4f09b-928f-42a3-a8f6-61619a603f7d" width="24%" style="display: block;"/>
  <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/87e8b4fe-7509-4501-96a2-38c257c333a4" width="24%" style="display: block;"/>
  <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/9fc61ab5-b455-4cf2-b9e9-b60ce40310a3" width="24%" style="display: block;"/>
  <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/8bcd2427-5a32-4fec-8839-a4e398e2602d" width="24%" style="display: block;"/>
</div>

- 바코드 스캔 : 식품 및 의약품의 바코드를 카메라로 인식하여 원재료 목록을 자동으로 불러옴

<h3 id="feature4">알레르기 항원 추론 (Allergy Antigen Inference)</h3>

> 알레르기 프로필과 일기장으로부터 알레르기 항원을 추론할 수 있는 기능입니다.

<div>
  <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/d5296909-6dde-4965-a252-403651267556" width="24%" style="display: block;"/>
  <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/ecad9390-dea6-4258-9d04-540517094084" width="24%" style="display: block;"/>
</div>

- 알레르기 항원 추론 : 알레르기 일기장에 존재하는 섭취 식품 및 복용 의약품 정보와 알레르기 반응 기록을 분석하여 알레르기 항원을 추론함

<h2 id="prerequisites"> :fork_and_knife: 컴퓨터 구성 / 필수 조건 안내 (Prerequisites)</h2>
<h3> :earth_asia: Browser</h3>

| <img src="https://user-images.githubusercontent.com/55467050/137036906-a6c0f879-5b51-49d3-8e02-d01994f64d18.png" alt="Chrome" width="16px" height="16px" /> Chrome | <img src="https://user-images.githubusercontent.com/55467050/137036913-033a988f-b9c9-4980-8540-5994cfa7e465.jpg" alt="Edge" width="16px" height="16px" /> Edge | <img src="https://user-images.githubusercontent.com/55467050/137036914-1a1f080e-9fb3-4b29-a143-517be979e78f.png" alt="Safari" width="16px" height="16px" /> Safari | <img src="https://user-images.githubusercontent.com/55467050/137036916-91328771-5dd5-41fb-a842-8562db3c480c.png" alt="Firefox" width="16px" height="16px" /> Firefox |
| :---------: | :---------: | :---------: | :---------: |
| Yes | Yes | Yes | Yes |


<h3> 💾 Versions</h3>

| <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/6a36fe11-cbd4-4f2c-a181-beca84756d97" width="16px" height="16px" /> Spring boot | <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/dee9ffa7-242f-4ad0-8f56-f5cce38536f3"  width="16px" height="16px" /> MariaDB | <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/1dfa7249-0d81-40e4-96fa-94595f3c2c48" width="16px" height="16px" /> NodeJS | <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/0b3dec8f-6fe5-4ea7-9e45-a069ea94b85d" width="16px" height="16px" /> React | <img src="https://github.com/MarmotCluster/AllergySafe-client/assets/87087163/50867847-a5ce-4374-ae06-b0c9e2bbc279" width="16px" height="16px" /> MUI |
| :---------: | :---------: | :---------: | :---------: | :---------: |
| 2.7+ | 10+ | 17+ | 18.2.0+ | 5.14.3+ |
<br/>

<h2 id="techniques"> 🧱 기술 스택 (Technique Used)</h2>

![techstack](https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/f58451d3-cc7e-4bce-91e9-50a8bda33878)

### Backend
- `Java` 백엔드 구축:
  - [`Spring Boot`](https://spring.io/projects/spring-boot) — Spring Boot 프레임워크 사용.
- Database:
  - [`MariaDB`](https://mariadb.org) — MariaDB로 데이터베이스 구축.

### Frontend
- [React](https://reactjs.org/)를 이용한 프론트엔드 구축:
  - [`MUI`](https://mui.com/) — MUI(Material UI) 컴포넌트 라이브러리 활용.
  - `React router` — 컴포넌트 네비게이션에 사용.
- [Recoil](https://recoiljs.org/)을 통한 리액트 상태 관리:
  - `Atom` — 전역 상태 관리에 사용.

<h2 id="team"> 💁🏻‍♀️💁🏻‍♂️ 팀 정보 (Team Information)</h2>

<table width="900">
<thead>
<tr>
<th width="100" align="center">Profile</th>
<th width="100" align="center">Name</th>
<th width="250" align="center">Role</th>
<th width="150" align="center">Github</th>
<th width="300" align="center">E-mail</th>
</tr> 
</thead>
<tbody>

<tr>
<td width="100" align="center"><img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/68128b77-8c73-4a7f-92f8-f563b1988cd8" width="60" height="60"></td>
<td width="100" align="center">김태원</td>
<td width="250">Backend Engineer(팀장)</td>
<td width="150" align="center">	
	<a href="https://github.com/dev-taewon-kim">
	<img src="https://img.shields.io/badge/devtaewonkim-655ced?style=social&logo=github"/>
	</a>
</td>
<td width="300" align="center">
<a href="mailto:hobang6@naver.com">hobang6@naver.com<a>
</tr>

<tr>
<td width="100" align="center"><img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/bbe61194-c875-499d-b88b-6433b4f61b40" width="60" height="60"></td>
<td width="100" align="center">전유나</td>
<td width="250">Backend Engineer</td>
<td width="150" align="center">	
	<a href="https://github.com/newFantasy">
	<img src="https://img.shields.io/badge/newFantasy-655ced?style=social&logo=github"/>
	</a>
</td>
<td width="300" align="center">
<a href="mailto:skdbwjs111@naver.com">skdbwjs111@naver.com<a>
</tr>

<tr>
<td width="100" align="center"><img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/50cce390-810d-4949-9be0-6bd47708e7b8" width="60" height="60"></td>
<td width="100" align="center">유지상</td>
<td width="250">Backend Engineer</td>
<td width="150" align="center">	
	<a href="https://github.com/You-jisang">
	<img src="https://img.shields.io/badge/Youjisang-655ced?style=social&logo=github"/>
	</a>
</td>
<td width="300" align="center">
<a href="mailto:dbwltkd1019@naver.com">dbwltkd1019@naver.com<a>
</tr>

<tr>
<td width="100" align="center"><img src="https://github.com/AllergySafe-server/AllergySafe-server/assets/85913822/675ef88c-252d-403a-84ed-59a8b5681430" width="60" height="60"></td>
<td width="100" align="center">권순범</td>
<td width="250">Frontend Engineer</td>
<td width="150" align="center">	
	<a href="https://github.com/MarmotCluster">
	<img src="https://img.shields.io/badge/MarmotCluster-655ced?style=social&logo=github"/>
	</a>
</td>
<td width="300" align="center">
<a href="mailto:wasabictangentation@naver.com">wasabictangentation@naver.com<a>
</tr>

</tbody>
</table>
</br>
