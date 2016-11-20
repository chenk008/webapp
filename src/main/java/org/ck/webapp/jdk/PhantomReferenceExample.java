package org.ck.webapp.jdk;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceExample {

	public static void main(String[] args) throws InterruptedException {

		// 1.�������ö���ReferenceQueue
		ReferenceQueue queue = new ReferenceQueue();
		System.out.println("1.ȷ��[���ö���]��" + queue + "\n");

		// 2.����Ŀ��
		ReferenceTarget referenceTarget = new ReferenceTarget();
		System.out.println("2.ȷ��[����Ŀ��]��" + referenceTarget + "\n");

		// 3.����һ�������ã��ɹ��캯������һ������Ŀ�꣬����һ�����ö���
		Reference phantomReference = new PhantomReference(referenceTarget,
				queue);
		System.out.println("3.ȷ��[������]: " + phantomReference + "\n");

		// 4.ȷ������Ŀ��
		referenceTarget = null;
		System.out.println("4.��[������]��ȷ��[����Ŀ��]��" + phantomReference.get()
				+ "��ʵ������������ն�����null\n");

		// 5.��һ������GC����Ϣ1��
		System.out.println("5.��һ������GC����Ϣ1��\n");
		System.gc();
		Thread.sleep(1000);

		try {
			// 6.ִ������Ŀ�궨����ս᷽�������������ս᷽���Ѿ������й���
			if (ReferenceTarget.class.getDeclaredMethod("finalize") != null)
				;
			System.out.println("6.����Ŀ�����һ��finalize����������ִ����\n");

			// 7.��һ�δ����ö���ȡ�����ö���
			System.out.println("7.��һ�δ����ö���ȡ�����ö��� " + queue.poll());
			System.out.println("��ǰ[����Ŀ��]����һ��ǿ�ɵ������");
			System.out.println("��ǰ[����Ŀ��]����һ����ɵ������");
			System.out.println("��ǰ[����Ŀ��]����һ�����ɵ������");
			System.out.println("��ǰ[����Ŀ��]��һ��[������]������");
			System.out.println("��ǰ[����Ŀ��]��finalize�Ѿ����й�\n");

			// 8.�ڶ�������GC����Ϣ1��
			System.out.println("8.�ڶ�������GC����Ϣ1��" + "\n");

			System.gc();
			Thread.sleep(1000);

			// 9.��ǰ����Ŀ����һ����ɵ������
			System.out.println("9.��ǰ����Ŀ����һ����ɵ������\n");

			// 10.�ڶ��δ����ö���ȡ�����ö���
			System.out.println("10.�ڶ��δ����ö���ȡ�����ö���" + queue.poll() + "\n");

		} catch (Exception e) {
			e.printStackTrace();
			// ע���ɳ������ε�ReferenceTarget��finalize()�������������д�����
			System.out.println("6.Ŀ�����û�ж���finalize()����\n");

			// 7.����Ŀ��������ɵ���״̬��GC�����Ѹ������ü��뵽���ö�����
			System.out.println("7.��һ�δ����ö���ȡ�����ö���" + queue.poll() + "\n");
		}
	}

	public static class ReferenceTarget {

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			System.out.println("finalizing����");
		}

	}
}
