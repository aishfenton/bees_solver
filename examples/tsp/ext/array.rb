class Array
  def rand
    self[Kernel.rand(self.size)]
  end

  def delete_rand
    idx = Kernel.rand(self.size)
    self.delete_at(idx)
  end

  def insert_rand(value)
    idx = Kernel.rand(self.size)
    self.insert(idx, value)
  end

end
