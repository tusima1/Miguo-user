package com.fanwe.model;

import java.util.List;

public class Uc_Xiaomi_ActModel extends BaseActModel
{
		//我的 小米
		private List<Member> list;
		private PageModel page;
		private int level1;
		private int level2;
		private int level3;
		private int level2_1;
		
		private int up_id;
		private String up_name;
		
		public int getUp_id() {
			return up_id;
		}
		public void setUp_id(int up_id) {
			this.up_id = up_id;
		}
		public String getUp_name() {
			return up_name;
		}
		public void setUp_name(String up_name) {
			if (getUp_id() == -1) {
				this.up_name="无";
			}else{
				this.up_name = up_name;
			}
		}
		public int getLevel2_1() {
			return level2_1;
		}
		public void setLevel2_1(int level2_1) {
			this.level2_1 = level2_1;
		}
		public int getLevel1() {
			return level1;
		}
		public void setLevel1(int level1) {
			this.level1 = level1;
		}
		public int getLevel2() {
			return level2;
		}
		public void setLevel2(int level2) {
			this.level2 = level2;
		}
		public int getLevel3() {
			return level3;
		}
		public void setLevel3(int level3) {
			this.level3 = level3;
		}
		public void setList(List<Member> list)
		{
			this.list = list;
		}
		public PageModel getPage() 
		{
			return page;
		}
		public void setPage(PageModel page)
		{
			this.page = page;
		}
		public List<Member> getList()
		{
			return list;
		}
		public void setResult(List<Member>list) 
		{
			this.list = list;
		}
}
