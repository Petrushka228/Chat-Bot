import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fight {
	private Pokemon myPoke;
	private Pokemon enemy;
	private Data data;
	private boolean isMyTurn;
	private boolean fightPhaseComplited;
	private boolean enemyChousen;
	
	public Fight(Pokemon me, Data dt)
	{
		myPoke = me;
		enemy = null;
		data = dt;
		fightPhaseComplited = false;
		enemyChousen = false;
	}
	
	public boolean isComplited()
	{
		return fightPhaseComplited;
	}
	
	public OutputData next(String input)
	{
		if(enemy != null && (myPoke.isDead() || enemy.isDead()))
		{
			fightPhaseComplited = true;
			if(myPoke.isDead())
				return new OutputData("��������!");
			else
				return new OutputData("�� �������, �������!");
		}
		
		if ("/fight".equals(input))
		{
			fightPhaseComplited = false;
			enemyChousen = false;
			return choseEnemy();
		}
		if(!enemyChousen)
		{
			//��� ������ ����� ��������� ���������: ������ ����� - ����� �������; 
			//������ - ����� �������� ������, ������� ������ ����
			//����� ��� ��� ������������, �� ������ ����� �� ������� ����
			if("00".equals(input) || "11".equals(input))
			{				
				enemy = data.getRandomPokemon();
				ArrayList<String> answers = new ArrayList<String>();
				answers.add("��");
				answers.add("���");
				return new OutputData(1, Arrays.asList("���� ���������: " + enemy.getName(), "\n", enemy.getInfo(), "����������?"), answers, enemy.getPhoto());
			}
			if("01".equals(input))
				return new OutputData("���� ������ ��� � ����������");
			if("10".equals(input))
			{
				enemyChousen = true;
				isMyTurn = myPoke.getSpeed() > enemy.getSpeed() ? true : false;
				if(isMyTurn)
					return userTurn(0);
				else
					return enemyTurn();
			}
		}
		if("5".equals(input.substring(0, 1)))
		{
			if(isMyTurn)
				return userTurn(Integer.parseInt(input.substring(1)));
			return enemyTurn();
		}
		return null;
		
	}
	
	private OutputData enemyTurn() 
	{
		int damage = enemy.useRandomSkill(myPoke.getCharacteristics());
		myPoke.hurt(damage);
		isMyTurn = true;
		return new OutputData(5, Arrays.asList("��������� ����� ����: (" + damage + ") ��.", "���� ���!"), myPoke.getSkillsList());
	}

	private OutputData userTurn(int skillIndex) {
		int damage = myPoke.useSkill(skillIndex, enemy.getCharacteristics());
		enemy.hurt(damage);
		isMyTurn = false;
		OutputData enemyTurn = enemyTurn();
		isMyTurn = true;
		List<String> text = Arrays.asList("���� ������� ����� ����: (" + damage + ") ��.", "��� ����������!");
		text.addAll(enemyTurn.getMessage());
		return new OutputData(5, text, myPoke.getSkillsList());
	}

	private OutputData choseEnemy() 
	{
		ArrayList<String> answers = new ArrayList<String>();
		answers.add("��������� ���������");
		answers.add("������������");
		return new OutputData(0, Arrays.asList("� ��� ������� ���������?"), answers);
	}
}
