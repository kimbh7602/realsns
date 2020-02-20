package edu.ssafy.boot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.ssafy.boot.dto.BlockVo;
import edu.ssafy.boot.dto.LogVo;

@Service("BlockchainService")
public class BlockchainService implements IBlockchainService {

	
	private List<BlockVo> chain;
	
	public BlockchainService() {
		chain = new ArrayList<BlockVo>();
		chain.add(generateGenesis());
		LogVo log = new LogVo("smapleid1","sampleip","회원가입");
		BlockVo block = new BlockVo(log,"2020.02.20 12:33:42");
		addBlock(block);
		
		LogVo log1 = new LogVo("smapleid2","sampleip","회원가입");
		BlockVo block1 = new BlockVo(log1,"2020.02.19 21:15:42");
		addBlock(block1);
		
		LogVo log2 = new LogVo("smapleid3","sampleip","회원가입");
		BlockVo block2 = new BlockVo(log2,"2020.02.19 15:47:42");
		addBlock(block2);
		LogVo log3 = new LogVo("smapleid4","sampleip","회원가입");
		BlockVo block3 = new BlockVo(log3,"2020.02.18 11:49:42");
		addBlock(block3);
		LogVo log4 = new LogVo("smapleid5","sampleip","회원가입");
		BlockVo block4 = new BlockVo(log4,"2020.02.18 02:21:42");
		addBlock(block4);
		LogVo log5 = new LogVo("smapleid6","sampleip","회원가입");
		BlockVo block5 = new BlockVo(log5,"2020.02.17 16:42:42");
		addBlock(block5);
		LogVo log6 = new LogVo("smapleid7","sampleip","회원가입");
		BlockVo block6 = new BlockVo(log6,"2020.02.17 16:42:42");
		addBlock(block6);
		LogVo log7 = new LogVo("smapleid8","sampleip","회원가입");
		BlockVo block7 = new BlockVo(log7,"2020.02.17 16:42:42");
		addBlock(block7);
		LogVo log8 = new LogVo("smapleid9","sampleip","회원가입");
		BlockVo block8 = new BlockVo(log8,"2020.02.17 16:42:42");
		addBlock(block8);
		LogVo log9 = new LogVo("smapleid10","sampleip","회원가입");
		BlockVo block9 = new BlockVo(log9,"2020.02.17 12:42:42");
		addBlock(block9);
		LogVo log10 = new LogVo("smapleid11","sampleip","회원가입");
		BlockVo block10 = new BlockVo(log10,"2020.02.16 11:42:42");
		addBlock(block10);
	}
	
	public List<BlockVo> getChain() {
		return chain;
	}

	//첫 블록생성
	private BlockVo generateGenesis() {
		BlockVo genesis = new BlockVo(null);
		genesis.setPrehash(null);
		genesis.computeHash();
		return genesis;
	}
	
	//블록생성 추가
	public void addBlock(BlockVo blk) {
		BlockVo newBlock = blk;
		newBlock.setPrehash(chain.get(chain.size() - 1).getHash());
		newBlock.computeHash();
		this.chain.add(newBlock);
	}
	
	//블록 확인용
	public void displayChain() {
		for (int i = 0; i < chain.size(); i++) {
			System.out.println("Blcok: " + i);
			System.out.println("Hash: " + chain.get(i).getHash());
			System.out.println("PreHash: " + chain.get(i).getPrehash());
			System.out.println("Data: " + chain.get(i).getData());
			System.out.println("Date: " + chain.get(i).getTimestamp());
		}
	}
	
	//블록신뢰성 확인
	public boolean isValid() {

		for (int i = chain.size() - 1; i > 0; i--) {
			if (!(chain.get(i).getHash().equals(chain.get(i).computeHash()))) {
				System.out.println("유효하지않은 체인");
				return false;
			}
			if (!(chain.get(i).getPrehash().equals(chain.get(i - 1).computeHash()))) {
				System.out.println("유효하지않은 체인");
				System.out.println(i);
				System.out.println(chain.get(i).getPrehash());
				System.out.println(chain.get(i - 1).computeHash());
				System.out.println(chain.get(1).computeHash());
				return false;
			}

		}
		System.out.println("유효한 체인");
		return true;
	}

}
